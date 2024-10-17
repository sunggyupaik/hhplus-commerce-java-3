package com.hhplus.commerce.domain.Item;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    List<ItemOption> itemOptions = new ArrayList<>();

    private String itemName;

    private Long itemPrice;

    @Builder
    public Item(
            Long id,
            String itemName,
            Long itemPrice
    ) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public Item addItemOption(ItemOption itemOption) {
        itemOptions.add(itemOption);
        itemOption.changeItem(this);
        return this;
    }
}
