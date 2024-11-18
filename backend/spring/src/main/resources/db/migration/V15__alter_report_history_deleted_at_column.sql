ALTER TABLE report_history
    ALTER COLUMN deleted_at DROP DEFAULT,
    ALTER COLUMN deleted_at DROP NOT NULL;
