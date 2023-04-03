package com.project.constant;

public enum ItemSellStatus {
    SELL("판매중"), SOLD_OUT("품절"), WAIT("대기"), CONFIRM("승인"), REFUSE("반려"), CLOSE("종료");
	//수정 2023.04.03 미경
    private String status;

    private ItemSellStatus(String status) {
        this.status = status;
    }

    public boolean isSell() {
        return this == SELL;
    }

    public String getStatus() {
        return status;
    }
}
