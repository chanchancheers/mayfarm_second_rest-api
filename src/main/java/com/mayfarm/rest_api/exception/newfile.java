//public class ExceptionController {
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> handleException(Exception ex){
//        ErrorResult errorResult = new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), ErrorCode.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> handleNullPointerException(NullPointerException  ex){
//        ErrorResult errorResult = new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),ErrorCode.BAD_REQUEST.name(), ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
//    }
//
//    private void ValidCheck(SearchParam searchParam) throws RuntimeException {
//        if(searchParam.getURL() == null){
//            CrawlState = 4;
//            throw new RuntimeException("SeedURL을 확인해주세요.");
//        } else if(searchParam.getCrawlingDepth() < -1){
//            CrawlState = 4;
//            throw new RuntimeException("Depth는 최소 -1이고 0또는 양수여야 합니다.");
//        } else if(searchParam.getConnectionTimeout() < 0){
//            CrawlState = 4;
//            throw new RuntimeException("connectionTimeout은 최소 1초 이상이어야 합니다.");
//        }
//    }