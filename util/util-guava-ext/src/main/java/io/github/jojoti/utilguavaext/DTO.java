package io.github.jojoti.utilguavaext;

/**
 * 返回唯一 Key
 * 1.此 interface 用于定义项目的唯一错误码使用枚举实现该接口
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class DTO<K extends ErrorKey, T> {

    private final K err;
    private final T data;

    private DTO(K err, T data) {
        this.err = err;
        this.data = data;
    }

    public static <K extends ErrorKey, T> DTO<K, T> ok(K code, T data) {
        return new DTO<>(code, data);
    }

    public static <K extends ErrorKey, T> DTO<K, T> ok() {
        return new DTO<>(null, null);
    }

    public static <K extends ErrorKey, T> DTO<K, T> ok(T data) {
        return new DTO<>(null, data);
    }

    public static <K extends ErrorKey, T> DTO<K, T> error(K err) {
        return new DTO<>(err, null);
    }

    public static <K extends ErrorKey, T> DTO<K, T> error(K err, T data) {
        return new DTO<>(err, data);
    }

    public boolean isOk() {
        return err == null || err.isOk();
    }

    public boolean isFailed() {
        return this.err != null && this.err.isError();
    }

    public T getData() {
        return data;
    }

    public K errorCode() {
        return err;
    }

    public <KK extends ErrorKey, T_NEW> DTO<KK, T_NEW> errorConvertNewType() {
        return (DTO<KK, T_NEW>) this;
    }

    @Override
    public String toString() {
        return "DTO{" +
                "code=" + err +
                ", data=" + data +
                '}';
    }

}