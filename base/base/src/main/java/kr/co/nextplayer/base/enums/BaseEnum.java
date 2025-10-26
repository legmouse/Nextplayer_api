package kr.co.nextplayer.base.enums;

/**
 * 공통 Enum interface
 */
public interface BaseEnum<T> {

    /**
     * code에 해당하는 설명을 리턴
     * @return
     */
    String description();

    /**
     * code값을 리턴
     * @return
     */
    T code();

    /**
     * Enum명을 리턴
     * @return
     */
    String name();


}
