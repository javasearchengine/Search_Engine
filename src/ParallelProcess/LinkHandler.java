package ParallelProcess;

public interface LinkHandler {

    int size();
    boolean visited(String link);
    void addVisited(String link);
}