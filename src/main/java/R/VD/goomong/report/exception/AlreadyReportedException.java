package R.VD.goomong.report.exception;

public class AlreadyReportedException extends RuntimeException {

    public AlreadyReportedException() {
        super();
    }

    public AlreadyReportedException(String message) {
        super(message);
    }
}
