package net.moubiecat.chatcontrolred.menu;

public enum MenuSize {
    ONE(9),     // 一行
    TWO(18),    // 兩行
    THREE(27),  // 三行
    FOUR(36),   // 四行
    FIVE(45),   // 五行
    SIX(54);    // 六行

    // 大小
    private final int size;

    /**
     * @param size 大小
     */
    MenuSize(int size) {
        this.size = size;
    }

    /**
     * 返回表示大小
     *
     * @return 大小
     */
    public final int getSize() {
        return this.size;
    }
}
