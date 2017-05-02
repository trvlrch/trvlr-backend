INSERT INTO trvlr.station (name) VALUES
  ('Winterthur'),
  ('Zurich'),
  ('Bern');

# public chats
INSERT INTO trvlr.chat_room (trvlr.chat_room.from, trvlr.chat_room.to) VALUES
  (1, 2),
  (2, 1),
  (3, 2),
  (2, 3);

# private chat
INSERT INTO trvlr.chat_room (trvlr.chat_room.from, trvlr.chat_room.to) VALUES
  (null, null);