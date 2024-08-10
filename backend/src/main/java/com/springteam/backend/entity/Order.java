package com.springteam.backend.entity;

import com.springteam.backend.dto.DataChartDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
@SqlResultSetMapping(name = "DataChartMapping",
        classes = @ConstructorResult(
                targetClass = DataChartDto.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "data", type = long.class)
                }
        ))
@NamedNativeQuery(
        name = "Order.getLastWeekRevenue",
        query = "select pc1_0.category_name as name ,coalesce(sum(o1_0.total_price),0) as data " +
                "from orders o1_0 right join order_detail od1_0 on o1_0.order_id=od1_0.order_id " +
                "right join product p1_0 on od1_0.product_id=p1_0.product_id " +
                "right join category pc1_0 on p1_0.category_id=pc1_0.category_id and YEARWEEK(o1_0.date, 1) = YEARWEEK(DATE_SUB(CURRENT_DATE(), INTERVAL 1 WEEK), 1) " +
                "group by pc1_0.category_name;",
        resultSetMapping = "DataChartMapping"
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    private LocalDate date;
    private Long totalPrice;
    private String customerFullName;
    private String address;
    private String email;
    private String phoneNumber;
    private boolean isPaid;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderDetail> orderDetails;

}
