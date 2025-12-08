ALTER TABLE heapdog_user
    ADD COLUMN current_membership_id BIGINT DEFAULT null;

ALTER TABLE heapdog_user
    ADD CONSTRAINT fk_current_membership
        FOREIGN KEY (current_membership_id)
            REFERENCES membership (id);