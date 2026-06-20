package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.dtos.*;
import com.alsaineybarry.tickets.domain.entities.Event;
import com.alsaineybarry.tickets.domain.entities.Ticket;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.enums.EventStatusEnum;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import com.alsaineybarry.tickets.repositories.EventRepository;
import com.alsaineybarry.tickets.repositories.TicketRepository;
import com.alsaineybarry.tickets.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImplSimple implements com.alsaineybarry.tickets.services.AdminService {
    
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    @Override
    public AdminReportDto.EventReportDto getEventReport() {
        // Get all events for statistics
        List<Event> allEvents = eventRepository.findAll();
        
        int totalEvents = allEvents.size();
        int activeEvents = (int) allEvents.stream().filter(event -> 
                event.getStart().isAfter(LocalDateTime.now())).count();
        int completedEvents = (int) allEvents.stream().filter(event -> 
                event.getStart().isBefore(LocalDateTime.now())).count();
        int cancelledEvents = (int) allEvents.stream().filter(event -> 
                "CANCELLED".equals(event.getStatus())).count();
        
        // Calculate total attendees and revenue
        int totalAttendees = 0;
        double totalRevenue = 0.0;
        List<AdminReportDto.EventReportDto.EventPerformanceDto> eventPerformances = new ArrayList<>();
        
        for (Event event : allEvents) {
            List<Ticket> tickets = ticketRepository.findByTicketTypeEventId(event.getId());
            int ticketsSold = tickets.size();
            double revenue = tickets.stream()
                    .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                    .sum();
            int attendeeCount = (int) tickets.stream()
                    .filter(ticket -> ticket.isCheckedIn())
                    .count();
            
            totalAttendees += attendeeCount;
            totalRevenue += revenue;
            
            AdminReportDto.EventReportDto.EventPerformanceDto performance = 
                    AdminReportDto.EventReportDto.EventPerformanceDto.builder()
                            .eventTitle(event.getName())
                            .organizerName(event.getOrganizer() != null ? event.getOrganizer().getName() : "Unknown")
                            .ticketsSold(ticketsSold)
                            .revenue(BigDecimal.valueOf(revenue))
                            .attendeeCount(attendeeCount)
                            .status(String.valueOf(event.getStatus()))
                            .build();
            
            eventPerformances.add(performance);
        }
        
        return AdminReportDto.EventReportDto.builder()
                .totalEvents(totalEvents)
                .activeEvents(activeEvents)
                .completedEvents(completedEvents)
                .cancelledEvents(cancelledEvents)
                .totalAttendees(totalAttendees)
                .totalRevenue(BigDecimal.valueOf(totalRevenue))
                .eventPerformances(eventPerformances)
                .build();
    }

    // Organizer Management
    @Override
    public Page<AdminOrganizerDto> getAllOrganizers(Pageable pageable) {
        Page<User> organizers = userRepository.findByRole(RoleEnum.ORGANIZER, pageable);
        return organizers.map(this::convertToAdminOrganizerDto);
    }

    @Override
    public AdminOrganizerDto getOrganizerById(UUID organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
        
        if (organizer.getRole() == null || organizer.getRole().getName() != RoleEnum.ORGANIZER) {
            throw new RuntimeException("User is not an organizer");
        }
        
        return convertToAdminOrganizerDto(organizer);
    }

    @Override
    public AdminOrganizerStatisticsDto getOrganizerStatistics() {
        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        
        Integer totalOrganizers = userRepository.countByRole(RoleEnum.ORGANIZER);
        Integer newOrganizersThisMonth = userRepository.countByRoleAndCreatedAfter(RoleEnum.ORGANIZER, monthStart);
        
        List<User> allOrganizers = userRepository.findByRole(RoleEnum.ORGANIZER, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        
        int totalEventsCreated = allOrganizers.stream()
                .mapToInt(organizer -> eventRepository.findByOrganizerId(organizer.getId()).size())
                .sum();
        
        int totalTicketsSold = allOrganizers.stream()
                .mapToInt(organizer -> {
                    var events = eventRepository.findByOrganizerId(organizer.getId());
                    return events.stream()
                            .mapToInt(event -> ticketRepository.findByTicketTypeEventId(event.getId()).size())
                            .sum();
                })
                .sum();
        
        double totalRevenue = allOrganizers.stream()
                .mapToDouble(organizer -> {
                    var events = eventRepository.findByOrganizerId(organizer.getId());
                    return events.stream()
                            .mapToDouble(event -> {
                                var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                                return tickets.stream()
                                        .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                                        .sum();
                            })
                            .sum();
                })
                .sum();
        
        List<AdminOrganizerDto> topPerformingOrganizers = getTopPerformingOrganizers(5);
        
        return AdminOrganizerStatisticsDto.builder()
                .totalOrganizers(totalOrganizers)
                .activeOrganizers(totalOrganizers)
                .newOrganizersThisMonth(newOrganizersThisMonth)
                .totalEventsCreated(totalEventsCreated)
                .totalTicketsSold(totalTicketsSold)
                .totalRevenue(totalRevenue)
                .totalProfit(totalRevenue)
                .lastUpdated(LocalDateTime.now())
                .topPerformingOrganizers(topPerformingOrganizers)
                .build();
    }

    @Override
    public List<AdminOrganizerDto> getTopPerformingOrganizers(int limit) {
        List<User> organizers = userRepository.findTopPerformingOrganizers(RoleEnum.ORGANIZER, PageRequest.of(0, limit));
        return organizers.stream()
                .map(this::convertToAdminOrganizerDto)
                .sorted((a, b) -> Double.compare(b.getTotalRevenue(), a.getTotalRevenue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public AdminOrganizerStatsDto getOrganizerStats(UUID organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
        
        var events = eventRepository.findByOrganizerId(organizer.getId());
        
        int totalEvents = events.size();
        int activeEvents = (int) events.stream()
                .filter(e -> e.getStart() != null && e.getStart().isAfter(LocalDateTime.now()))
                .count();
        int completedEvents = (int) events.stream()
                .filter(e -> e.getStart() != null && e.getStart().isBefore(LocalDateTime.now()))
                .count();
        
        int totalTicketsSold = events.stream()
                .mapToInt(event -> ticketRepository.findByTicketTypeEventId(event.getId()).size())
                .sum();
        
        double totalRevenue = events.stream()
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        return AdminOrganizerStatsDto.builder()
                .organizerId(organizer.getId())
                .organizerName(organizer.getName())
                .organizerEmail(organizer.getEmail())
                .totalEvents(totalEvents)
                .activeEvents(activeEvents)
                .completedEvents(completedEvents)
                .cancelledEvents(0)
                .totalTicketsSold(totalTicketsSold)
                .totalRevenue(BigDecimal.valueOf(totalRevenue))
                .averageTicketPrice(totalTicketsSold > 0 ? BigDecimal.valueOf(totalRevenue / totalTicketsSold) : BigDecimal.ZERO)
                .totalFeesPaid(BigDecimal.valueOf(totalRevenue * 0.05))
                .netEarnings(BigDecimal.valueOf(totalRevenue * 0.95))
                .averageAttendanceRate(0.0)
                .repeatCustomers(0)
                .lastEventDate(null)
                .joinedDate(organizer.getCreatedAt())
                .isVerified(false)
                .isApproved(true)
                .recentEvents(List.of())
                .build();
    }

    @Override
    @Transactional
    public AdminOrganizerDto verifyOrganizer(UUID organizerId) {
        AdminOrganizerDto organizer = getOrganizerById(organizerId);
        // Verification logic would go here when verification status is added to User entity
        return organizer;
    }

    @Override
    @Transactional
    public AdminOrganizerDto approveOrganizer(UUID organizerId) {
        AdminOrganizerDto organizer = getOrganizerById(organizerId);
        // Approval logic would go here when approval status is added to User entity
        return organizer;
    }

    @Override
    @Transactional
    public AdminOrganizerDto rejectOrganizer(UUID organizerId, String rejectionReason) {
        AdminOrganizerDto organizer = getOrganizerById(organizerId);
        // Rejection logic would go here when rejection status is added to User entity
        return organizer;
    }

    // Dashboard & Activity
    @Override
    public AdminPlatformStatsDto getPlatformStats() {
        List<Event> allEvents = eventRepository.findAll();
        List<Ticket> allTickets = ticketRepository.findAll();
        
        int totalUsers = getTotalUsersCount();
        int totalOrganizers = getTotalOrganizersCount();
        int totalEvents = allEvents.size();
        int totalTickets = allTickets.size();
        
        LocalDateTime now = LocalDateTime.now();
        int activeEvents = (int) allEvents.stream()
                .filter(e -> e.getStart() != null && e.getStart().isAfter(now))
                .count();
        int completedEvents = (int) allEvents.stream()
                .filter(e -> e.getStart() != null && e.getStart().isBefore(now))
                .count();
        
        double totalRevenue = allTickets.stream()
                .mapToDouble(ticket -> ticket.getTicketType() != null ? ticket.getTicketType().getPrice() : 0.0)
                .sum();
        
        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        double monthlyRevenue = allEvents.stream()
                .filter(event -> event.getCreatedAt() != null && event.getCreatedAt().isAfter(monthStart))
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        return AdminPlatformStatsDto.builder()
                .totalUsers(totalUsers)
                .totalOrganizers(totalOrganizers)
                .totalEvents(totalEvents)
                .totalTickets(totalTickets)
                .activeEvents(activeEvents)
                .completedEvents(completedEvents)
                .totalRevenue(BigDecimal.valueOf(totalRevenue))
                .monthlyRevenue(BigDecimal.valueOf(monthlyRevenue))
                .newUsersThisMonth(getNewUsersThisMonth())
                .newOrganizersThisMonth(getNewOrganizersThisMonth())
                .newEventsThisMonth((int) allEvents.stream()
                        .filter(e -> e.getCreatedAt() != null && e.getCreatedAt().isAfter(monthStart))
                        .count())
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Override
    public Page<AdminActivityLogDto> getActivityLog(Pageable pageable) {
        // Placeholder - requires ActivityLog entity
        return new PageImpl<>(List.of(), pageable, 0);
    }

    // User Management
    @Override
    public Page<AdminUserManagementDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToAdminUserManagementDto);
    }

    @Override
    public AdminUserManagementDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToAdminUserManagementDto(user);
    }

    @Override
    @Transactional
    public void blockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Blocking logic would go here when blocked status is added to User entity
        throw new UnsupportedOperationException("User blocking not implemented - requires User.status field");
    }

    @Override
    @Transactional
    public void unblockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Unblock logic would go here
        throw new UnsupportedOperationException("User unblocking not implemented - requires User.status field");
    }

    @Override
    @Transactional
    public void suspendUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Suspend logic would go here
        throw new UnsupportedOperationException("User suspending not implemented - requires User.status field");
    }

    @Override
    @Transactional
    public void unsuspendUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Unsuspend logic would go here
        throw new UnsupportedOperationException("User unsuspending not implemented - requires User.status field");
    }

    @Override
    @Transactional
    public void banUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Ban logic would go here
        throw new UnsupportedOperationException("User banning not implemented - requires User.status field");
    }

    // System Statistics
    @Override
    public Integer getTotalOrganizersCount() {
        return userRepository.countByRole(RoleEnum.ORGANIZER);
    }

    @Override
    public Integer getTotalUsersCount() {
        return Math.toIntExact(userRepository.count());
    }

    @Override
    public Integer getNewOrganizersThisMonth() {
        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        return userRepository.countByRoleAndCreatedAfter(RoleEnum.ORGANIZER, monthStart);
    }

    @Override
    public Integer getNewUsersThisMonth() {
        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        return userRepository.countByRoleAndCreatedAfter(RoleEnum.USER, monthStart);
    }

    // Helper methods for DTO conversion
    private AdminOrganizerDto convertToAdminOrganizerDto(User organizer) {
        var events = eventRepository.findByOrganizerId(organizer.getId());
        
        int totalEventsCreated = events.size();
        
        int totalTicketsSold = events.stream()
                .mapToInt(event -> ticketRepository.findByTicketTypeEventId(event.getId()).size())
                .sum();
        
        double totalRevenue = events.stream()
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        return AdminOrganizerDto.builder()
                .organizerId(organizer.getId())
                .organizerName(organizer.getName())
                .organizerEmail(organizer.getEmail())
                .createdAt(organizer.getCreatedAt())
                .totalEventsCreated(totalEventsCreated)
                .totalTicketsSold(totalTicketsSold)
                .totalRevenue(totalRevenue)
                .totalProfit(totalRevenue)
                .isActive(true)
                .build();
    }

    private AdminUserManagementDto convertToAdminUserManagementDto(User user) {
        int eventsAttended = user.getAttendingEvents() != null ? user.getAttendingEvents().size() : 0;
        int ticketsPurchased = ticketRepository.findByPurchaserId(user.getId(), Pageable.unpaged()).getContent().size();
        
        return AdminUserManagementDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().getName().toString() : "UNKNOWN")
                .createdAt(user.getCreatedAt())
                .lastLogin(null)
                .isActive(true)
                .isBlocked(false)
                .eventsAttended(eventsAttended)
                .ticketsPurchased(ticketsPurchased)
                .build();
    }

    // Payout Management - Placeholder implementations (requires Payout entity)
    @Override
    public Page<AdminPayoutDto> getAllPayouts(Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    public AdminPayoutDto getPayoutById(UUID payoutId) {
        throw new RuntimeException("Payout not found - requires Payout entity");
    }

    @Override
    @Transactional
    public AdminPayoutDto createPayout(AdminPayoutRequestDto payoutRequest) {
        throw new UnsupportedOperationException("Payout creation not implemented - requires Payout entity");
    }

    @Override
    @Transactional
    public AdminPayoutDto processPayout(UUID payoutId) {
        throw new UnsupportedOperationException("Payout processing not implemented - requires Payout entity");
    }

    @Override
    @Transactional
    public AdminPayoutDto completePayout(UUID payoutId) {
        throw new UnsupportedOperationException("Payout completion not implemented - requires Payout entity");
    }

    @Override
    @Transactional
    public AdminPayoutDto failPayout(UUID payoutId, String failureReason) {
        throw new UnsupportedOperationException("Payout failure not implemented - requires Payout entity");
    }

    @Override
    public AdminPayoutStatisticsDto getPayoutStatistics() {
        return AdminPayoutStatisticsDto.builder()
                .totalPayouts(0)
                .pendingPayouts(0)
                .processingPayouts(0)
                .completedPayouts(0)
                .failedPayouts(0)
                .totalAmount(BigDecimal.ZERO)
                .pendingAmount(BigDecimal.ZERO)
                .completedAmount(BigDecimal.ZERO)
                .failedAmount(BigDecimal.ZERO)
                .build();
    }

    // Transaction Management - Placeholder implementations (requires Transaction entity)
    @Override
    public Page<AdminTransactionDto> getAllTransactions(Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    public AdminTransactionDto getTransactionById(UUID transactionId) {
        throw new RuntimeException("Transaction not found - requires Transaction entity");
    }

    // Financial Management
    @Override
    public AdminFinancialsDto getFinancials() {
        List<Event> allEvents = eventRepository.findAll();
        
        double totalRevenue = allEvents.stream()
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        double monthlyRevenue = allEvents.stream()
                .filter(event -> event.getCreatedAt() != null && event.getCreatedAt().isAfter(monthStart))
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        return AdminFinancialsDto.builder()
                .totalRevenue(BigDecimal.valueOf(totalRevenue))
                .totalPayouts(BigDecimal.ZERO)
                .netProfit(BigDecimal.valueOf(totalRevenue * 0.95))
                .pendingPayouts(BigDecimal.ZERO)
                .completedPayouts(BigDecimal.ZERO)
                .failedPayouts(BigDecimal.ZERO)
                .totalTransactions(0)
                .completedTransactions(0)
                .pendingTransactions(0)
                .failedTransactions(0)
                .averageTransactionAmount(BigDecimal.ZERO)
                .currency("USD")
                .lastUpdated(LocalDateTime.now())
                .monthlySummaries(List.of())
                .build();
    }

    // Report Management
    @Override
    public AdminReportDto.UserReportDto getUserReport() {
        List<User> allUsers = userRepository.findAll();
        
        int totalUsers = allUsers.size();
        int activeUsers = totalUsers; // Assuming all users are active
        int newUsersThisMonth = getNewUsersThisMonth();
        int blockedUsers = 0; // No blocked users yet
        
        return AdminReportDto.UserReportDto.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .newUsersThisMonth(newUsersThisMonth)
                .blockedUsers(blockedUsers)
                .userActivities(List.of())
                .build();
    }

    @Override
    public AdminReportDto.RevenueReportDto getRevenueReport() {
        List<Event> allEvents = eventRepository.findAll();
        
        double totalRevenue = allEvents.stream()
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        double monthlyRevenue = allEvents.stream()
                .filter(event -> event.getCreatedAt() != null && event.getCreatedAt().isAfter(monthStart))
                .mapToDouble(event -> {
                    var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    return tickets.stream()
                            .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                            .sum();
                })
                .sum();
        
        return AdminReportDto.RevenueReportDto.builder()
                .totalRevenue(BigDecimal.valueOf(totalRevenue))
                .monthlyRevenue(BigDecimal.valueOf(monthlyRevenue))
                .weeklyRevenue(BigDecimal.ZERO)
                .dailyRevenue(BigDecimal.ZERO)
                .currency("USD")
                .revenueBreakdown(List.of())
                .build();
    }

    @Override
    public AdminReportDto.RefundReportDto getRefundReport() {
        // Placeholder - requires Refund entity
        return AdminReportDto.RefundReportDto.builder()
                .totalRefunds(0)
                .totalRefundAmount(BigDecimal.ZERO)
                .currency("USD")
                .refundRate(0.0)
                .refundDetails(List.of())
                .build();
    }

    // Event Management
    @Override
    public Page<AdminEventDto> getAllEvents(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(this::convertToAdminEventDto);
    }

    @Override
    public AdminEventDto getEventById(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToAdminEventDto(event);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(eventId);
    }

    @Override
    public Page<AdminEventDto> getPendingEvents(Pageable pageable) {
        // Assuming PENDING status events - filter by status if needed
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(this::convertToAdminEventDto);
    }

    @Override
    public AdminEventApprovalStatsDto getEventApprovalStats() {
        List<Event> allEvents = eventRepository.findAll();
        
        int totalEvents = allEvents.size();
        int pendingEvents = (int) allEvents.stream()
                .filter(e -> e.getStatus() == EventStatusEnum.DRAFT)
                .count();
        int approvedEvents = (int) allEvents.stream()
                .filter(e -> e.getStatus() == EventStatusEnum.PUBLISHED)
                .count();
        int rejectedEvents = (int) allEvents.stream()
                .filter(e -> e.getStatus() == EventStatusEnum.CANCELLED)
                .count();
        
        return AdminEventApprovalStatsDto.builder()
                .totalEvents(totalEvents)
                .pendingEvents(pendingEvents)
                .approvedEvents(approvedEvents)
                .rejectedEvents(rejectedEvents)
                .build();
    }

    @Override
    @Transactional
    public AdminEventDto approveEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatusEnum.PUBLISHED);
        eventRepository.save(event);
        return convertToAdminEventDto(event);
    }

    @Override
    @Transactional
    public AdminEventDto rejectEvent(UUID eventId, String rejectionReason) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatusEnum.CANCELLED);
        eventRepository.save(event);
        return convertToAdminEventDto(event);
    }

    @Override
    @Transactional
    public AdminEventDto disableEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatusEnum.CANCELLED);
        eventRepository.save(event);
        return convertToAdminEventDto(event);
    }

    private AdminEventDto convertToAdminEventDto(Event event) {
        var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
        int ticketsSold = tickets.size();
        double revenue = tickets.stream()
                .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                .sum();
        
        return AdminEventDto.builder()
                .eventId(event.getId())
                .eventTitle(event.getName())
                .organizerName(event.getOrganizer() != null ? event.getOrganizer().getName() : "Unknown")
                .organizerEmail(event.getOrganizer() != null ? event.getOrganizer().getEmail() : "Unknown")
                .eventDate(event.getStart())
                .venue(event.getVenue())
                .totalTickets(ticketsSold)
                .ticketsSold(ticketsSold)
                .revenue(BigDecimal.valueOf(revenue))
                .status(event.getStatus() != null ? event.getStatus().toString() : "UNKNOWN")
                .createdAt(event.getCreatedAt())
                .build();
    }

    // Tickets & Attendance
    @Override
    public Page<AdminTicketDto> getAllTickets(Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findAll(pageable);
        return tickets.map(this::convertToAdminTicketDto);
    }

    @Override
    public AdminCheckInStatsDto getEventCheckInStats(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
        int totalTickets = tickets.size();
        int checkedInTickets = (int) tickets.stream()
                .filter(Ticket::isCheckedIn)
                .count();
        
        return AdminCheckInStatsDto.builder()
                .eventId(eventId)
                .totalTickets(totalTickets)
                .checkedIn(checkedInTickets)
                .noShows(totalTickets - checkedInTickets)
                .pending(0)
                .checkInRate(totalTickets > 0 ? (double) checkedInTickets / totalTickets : 0.0)
                .build();
    }

    @Override
    public Page<AdminAttendeeDto> getEventAttendees(UUID eventId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
        List<AdminAttendeeDto> attendees = tickets.stream()
                .map(ticket -> AdminAttendeeDto.builder()
                        .attendeeId(ticket.getPurchaser() != null ? ticket.getPurchaser().getId() : null)
                        .ticketId(ticket.getId())
                        .userName(ticket.getPurchaser() != null ? ticket.getPurchaser().getName() : "Unknown")
                        .userEmail(ticket.getPurchaser() != null ? ticket.getPurchaser().getEmail() : "Unknown")
                        .ticketType(ticket.getTicketType() != null ? ticket.getTicketType().getName() : "Unknown")
                        .purchaseDate(ticket.getPurchaseDate())
                        .checkedIn(ticket.isCheckedIn())
                        .checkInTime(null)
                        .build())
                .collect(Collectors.toList());
        
        return new PageImpl<>(attendees, pageable, attendees.size());
    }

    @Override
    public byte[] exportAttendanceReport(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        var tickets = ticketRepository.findByTicketTypeEventId(event.getId());
        
        StringBuilder csv = new StringBuilder();
        csv.append("Ticket ID,Attendee Name,Attendee Email,Ticket Type,Purchase Date,Checked In\n");
        
        for (Ticket ticket : tickets) {
            csv.append(ticket.getId()).append(",")
               .append(ticket.getPurchaser() != null ? ticket.getPurchaser().getName() : "Unknown").append(",")
               .append(ticket.getPurchaser() != null ? ticket.getPurchaser().getEmail() : "Unknown").append(",")
               .append(ticket.getTicketType() != null ? ticket.getTicketType().getName() : "Unknown").append(",")
               .append(ticket.getPurchaseDate()).append(",")
               .append(ticket.isCheckedIn()).append("\n");
        }
        
        return csv.toString().getBytes();
    }

    private AdminTicketDto convertToAdminTicketDto(Ticket ticket) {
        return AdminTicketDto.builder()
                .ticketId(ticket.getId())
                .eventId(ticket.getTicketType() != null && ticket.getTicketType().getEvent() != null ? ticket.getTicketType().getEvent().getId() : null)
                .eventTitle(ticket.getTicketType() != null && ticket.getTicketType().getEvent() != null ? ticket.getTicketType().getEvent().getName() : "Unknown")
                .userId(ticket.getPurchaser() != null ? ticket.getPurchaser().getId() : null)
                .userName(ticket.getPurchaser() != null ? ticket.getPurchaser().getName() : "Unknown")
                .userEmail(ticket.getPurchaser() != null ? ticket.getPurchaser().getEmail() : "Unknown")
                .organizerId(ticket.getTicketType() != null && ticket.getTicketType().getEvent() != null && ticket.getTicketType().getEvent().getOrganizer() != null ? ticket.getTicketType().getEvent().getOrganizer().getId() : null)
                .organizerName(ticket.getTicketType() != null && ticket.getTicketType().getEvent() != null && ticket.getTicketType().getEvent().getOrganizer() != null ? ticket.getTicketType().getEvent().getOrganizer().getName() : "Unknown")
                .ticketTypeName(ticket.getTicketType() != null ? ticket.getTicketType().getName() : "Unknown")
                .price(ticket.getTicketType() != null ? BigDecimal.valueOf(ticket.getTicketType().getPrice()) : BigDecimal.ZERO)
                .currency("USD")
                .status(ticket.getStatus() != null ? ticket.getStatus().toString() : "UNKNOWN")
                .purchaseDate(ticket.getPurchaseDate())
                .usedDate(null)
                .refundDate(null)
                .checkedIn(ticket.isCheckedIn())
                .checkInTime(null)
                .build();
    }

    // Fee Settings - Placeholder implementations
    @Override
    public AdminFeeSettingsDto getFeeSettings() {
        return AdminFeeSettingsDto.builder()
                .platformFeePercentage(BigDecimal.valueOf(5.0))
                .paymentProcessingFeePercentage(BigDecimal.valueOf(2.9))
                .fixedPlatformFee(BigDecimal.valueOf(0.30))
                .fixedProcessingFee(BigDecimal.valueOf(0.30))
                .minimumFee(BigDecimal.ZERO)
                .maximumFee(BigDecimal.valueOf(100.0))
                .currency("USD")
                .enableDynamicPricing(false)
                .taxRate(BigDecimal.ZERO)
                .taxIncluded(false)
                .paymentGateway("stripe")
                .enableRefunds(true)
                .refundWindowDays(30)
                .refundFeePercentage(BigDecimal.valueOf(5.0))
                .build();
    }

    @Override
    @Transactional
    public AdminFeeSettingsDto updateFeeSettings(AdminFeeSettingsDto settings) {
        // In a real implementation, this would save to a database
        return settings;
    }

    // Admin Settings - Placeholder implementations
    @Override
    public AdminSettingsDto getAdminSettings() {
        return AdminSettingsDto.builder()
                .platformName("Event Ticket Platform")
                .platformEmail("admin@example.com")
                .supportEmail("support@example.com")
                .contactPhone("+1-555-0123")
                .timezone("UTC")
                .currency("USD")
                .enableEventApproval(true)
                .enableOrganizerVerification(true)
                .enableEmailNotifications(true)
                .enableSmsNotifications(false)
                .maxEventsPerOrganizer(100)
                .maxTicketsPerEvent(10000)
                .enableRefunds(true)
                .refundWindowDays(30)
                .enableTransfers(true)
                .transferWindowHours(24)
                .enableWaitlist(true)
                .maintenanceMode("disabled")
                .maintenanceStartTime(null)
                .maintenanceEndTime(null)
                .maintenanceMessage("")
                .enableAnalytics(true)
                .enableAuditLogs(true)
                .auditLogRetentionDays(90)
                .build();
    }

    @Override
    @Transactional
    public AdminSettingsDto updateAdminSettings(AdminSettingsDto settings) {
        // In a real implementation, this would save to a database
        return settings;
    }

    // Email Management - Placeholder implementations (requires EmailCampaign entity)
    @Override
    public List<AdminEmailTemplateDto> getEmailTemplates() {
        return List.of(
                AdminEmailTemplateDto.builder()
                        .templateId(UUID.randomUUID())
                        .templateName("Welcome Email")
                        .subject("Welcome to Event Ticket Platform")
                        .build(),
                AdminEmailTemplateDto.builder()
                        .templateId(UUID.randomUUID())
                        .templateName("Event Reminder")
                        .subject("Event Reminder")
                        .build()
        );
    }

    @Override
    @Transactional
    public AdminEmailCampaignDto createEmailCampaign(AdminEmailCampaignRequestDto campaignRequest) {
        throw new UnsupportedOperationException("Email campaign creation not implemented - requires EmailCampaign entity");
    }

    @Override
    public Page<AdminEmailCampaignDto> getEmailCampaigns(Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    public Page<AdminEmailRecipientDto> getCampaignRecipients(UUID campaignId, Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0);
    }

    // Refund Management - Placeholder implementations (requires Refund entity)
    @Override
    public Page<AdminRefundDto> getRefundRequests(UUID eventId, Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    @Transactional
    public AdminRefundDto approveRefund(UUID refundId) {
        throw new UnsupportedOperationException("Refund approval not implemented - requires Refund entity");
    }

    @Override
    @Transactional
    public AdminRefundDto rejectRefund(UUID refundId, String rejectionReason) {
        throw new UnsupportedOperationException("Refund rejection not implemented - requires Refund entity");
    }

    @Override
    public AdminRefundStatsDto getRefundStatistics(UUID eventId) {
        return AdminRefundStatsDto.builder()
                .totalRefunds(0)
                .pendingRefunds(0)
                .approvedRefunds(0)
                .rejectedRefunds(0)
                .totalRefundAmount(BigDecimal.ZERO)
                .build();
    }
}
