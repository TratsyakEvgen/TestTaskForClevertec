package ru.clevertec.check.repository.connection.pool;


public class PoolData {
    private final String url;
    private final String user;
    private final String password;
    private final int poolSize;

    private PoolData(String url, String user, String password, int poolSize) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;
    }


    public static PoolData.PoolDataBuilder builder() {
        return new PoolData.PoolDataBuilder();
    }

    public static class PoolDataBuilder {
        private String url;
        private String user;
        private String password;
        private int poolSize;


        public PoolData.PoolDataBuilder url(String url) {
            this.url = url;
            return this;
        }

        public PoolData.PoolDataBuilder user(String user) {
            this.user = user;
            return this;
        }

        public PoolData.PoolDataBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PoolData.PoolDataBuilder poolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public PoolData build() {
            return new PoolData(this.url, this.user, this.password, this.poolSize);
        }

    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolSize() {
        return poolSize;
    }
}
