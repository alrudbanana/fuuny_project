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
    //23.04.03 추기 ItemsellStatus 가 null이어도 검색 가능하게 null 추가 
    public static ItemSellStatus fromStatus(String status) {
        for (ItemSellStatus itemSellStatus : ItemSellStatus.values()) {
            if (itemSellStatus.getStatus().equals(status)) {
                return itemSellStatus;
            }
        }
        return null;
    }
}
