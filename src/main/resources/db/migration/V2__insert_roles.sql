INSERT INTO roles (id, authority)
VALUES ('754e9a65-bb43-487f-96e1-1f9c8449bc45', 'USER'),('bdf66d26-b743-4a56-a0ba-f516c895e090', 'ADMIN')
ON CONFLICT (authority) DO NOTHING;