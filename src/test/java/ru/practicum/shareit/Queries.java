package ru.practicum.shareit;

public interface Queries {
    String ADD_USER = "insert into users (email, user_name) values ('User1@email.com', 'User1')";
    String ADD_USER_2 = "insert into users (email, user_name) values ('User2@email.com', 'User2')";
    String ADD_ITEM_BOOKING_ITEM1_USER2 =
            "insert into items (available, description, item_name, owner_id) " +
                    "values ('true', 'Test item1', 'Item1', 1); " +
                    "insert into bookings (end_time, start_time, status, booker_id, item_id) " +
                    "values ('2021-12-18 19:26:21.000000', '2020-12-18 19:26:27.000000', 'APPROVED', 2, 1)";
    String RESET_USERS_ID = "alter table users alter column user_id restart with 1;";
    String RESET_ITEMS_ID = "alter table items alter column item_id restart with 1;";
}
