package ru.practicum.shareit;

public interface Queries {
    String ADD_USER = "insert into users (email, user_name) values ('User1@email.com', 'User1')";
    String ADD_USER_2 = "insert into users (email, user_name) values ('User2@email.com', 'User2')";

    String ADD_ITEM =
            "insert into items (available, description, item_name, owner_id) " +
                    "values ('true', 'Test item1', 'Item1', 1);";
    String ADD_ITEM_NOT_AVAILABLE =
            "insert into items (available, description, item_name, owner_id) " +
                    "values ('false', 'Test item1', 'Item1', 1);";
    String ADD_BOOKING_ITEM1_USER2 =
            "insert into bookings (end_time, start_time, status, booker_id, item_id) " +
                    "values ('2021-12-18 19:28:21.000000', '2020-12-18 19:26:27.000000', 'APPROVED', 2, 1)";
    String PAST_BOOKING =
            "insert into bookings (end_time, start_time, status, booker_id, item_id) " +
                    "values ('2022-10-19 00:32:30.000000', '2022-09-19 00:32:51.000000', 'APPROVED', 2, 1)";
    String CURRENT_BOOKING =
            "insert into bookings (end_time, start_time, status, booker_id, item_id) " +
                    "values ('3000-12-19 00:34:04.000000', '2022-11-19 00:33:16.000000', 'APPROVED', 2, 1)";
    String REJECTED_BOOKING =
            "insert into bookings (end_time, start_time, status, booker_id, item_id) " +
                    "values ('2021-12-19 00:34:28.000000', '2020-12-19 00:34:52.000000', 'REJECTED', 2, 1)";
    String WAITING_BOOKING =
            "insert into bookings (end_time, start_time, status, booker_id, item_id) " +
                    "values ('3010-12-19 00:37:08.000000', '3002-12-19 00:36:51.000000', 'WAITING', 2, 1)";

    String RESET_IDS =
            "alter table users alter column user_id restart with 1;" +
                    "alter table items alter column item_id restart with 1;" +
                    "alter table item_requests alter column request_id restart with 1;" +
                    "alter table bookings alter column booking_id restart with 1;";
}
