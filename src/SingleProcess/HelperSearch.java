package SingleProcess;

import java.util.List;
 
public class HelperSearch{
 
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }
 
    public static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }
 
    public static class Result {
        private String url;
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String toString() { return "Result[url:" + url + "]"; }
    }
}
