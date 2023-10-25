package R.VD.goomong.report.exception;

public class AlreadyDeletedReportException extends RuntimeException {

    public AlreadyDeletedReportException() {
        super();
    }

    public AlreadyDeletedReportException(String message) {
        super(message);
    }
}
