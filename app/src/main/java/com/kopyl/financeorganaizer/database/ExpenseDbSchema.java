package com.kopyl.financeorganaizer.database;

public class ExpenseDbSchema {
    public static final class ExpenseTable {
        public static final String NAME = "expenses";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String DATE = "date";
            public static final String COST = "cost";
            public static final String IS_COMING = "is_coming";
        }
    }
    public static final class MonthLimitTable {
        public static final String NAME = "month_limits";
        public static final class Cols {
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String LIMIT = "m_limit";
        }
    }
}
