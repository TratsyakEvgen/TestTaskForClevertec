package ru.clevertec.check.repository.connection.pool;

import java.sql.Connection;

public interface ConnectionPool {
    void initPoolData(PoolData poolData);

    Connection takeConnection();

    void destroy();

}
