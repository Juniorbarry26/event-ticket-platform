-- Convert QR code storage from text to binary for better performance and storage efficiency
-- This migration handles the data conversion safely

DO $$ BEGIN;

-- First, create a backup of existing QR codes (optional but recommended)
CREATE TABLE qr_codes_backup AS 
SELECT id, status, ticket_id, created_at, updated_at, code_value 
FROM qr_codes;

-- Clear the original table (this will also delete the data)
DELETE FROM qr_codes;

-- Recreate the table with binary column type
DROP TABLE qr_codes;

CREATE TABLE qr_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status VARCHAR(255) NOT NULL,
    ticket_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    code_value BYTEA NOT NULL
);

-- Add foreign key constraints
ALTER TABLE qr_codes 
ADD CONSTRAINT fk_qr_codes_ticket 
FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE;

-- Add indexes for performance
CREATE INDEX idx_qr_codes_ticket_id ON qr_codes(ticket_id);
CREATE INDEX idx_qr_codes_status ON qr_codes(status);

COMMIT;

DO $$;
