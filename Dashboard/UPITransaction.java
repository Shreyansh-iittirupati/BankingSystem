    package Dashboard;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

    public class UPITransaction {
        private String transactionId;
        private String fromUpiId;
        private String toUpiId;
        private BigDecimal amount;
        private String description;
        private String status;
        private LocalDateTime timestamp;

        public UPITransaction(String transactionId, String fromUpiId, String toUpiId,
                              BigDecimal amount, String description) {
            this.transactionId = transactionId;
            this.fromUpiId = fromUpiId;
            this.toUpiId = toUpiId;
            this.amount = amount;
            this.description = description;
            this.status = "PENDING";
            this.timestamp = LocalDateTime.now();
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getFromUpiId() {
            return fromUpiId;
        }

        public String getToUpiId() {
            return toUpiId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }


        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "UPITransaction{" +
                    "transactionId='" + transactionId + '\'' +
                    ", fromUpiId='" + fromUpiId + '\'' +
                    ", toUpiId='" + toUpiId + '\'' +
                    ", amount=" + amount +
                    ", description='" + description + '\'' +
                    ", status='" + status + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UPITransaction that = (UPITransaction) o;

            return transactionId.equals(that.transactionId);
        }

        @Override
        public int hashCode() {
            return transactionId.hashCode();
        }
    }