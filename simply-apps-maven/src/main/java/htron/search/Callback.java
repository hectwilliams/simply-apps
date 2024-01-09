package htron.search;

public interface Callback<T> {
    public static int interval = 500; 
    public void execute(T t);

}