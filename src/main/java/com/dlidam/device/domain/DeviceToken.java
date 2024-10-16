package com.dlidam.device.domain;


import com.dlidam.user.domain.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "deviceToken"})
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_device_token_user"))
    private User user;

    @Column(name = "device_token", unique = true, nullable = false)
    private String deviceToken;

    public DeviceToken(final User user, final String deviceToken){
        this.user = user;
        this.deviceToken = deviceToken;
    }

    public boolean isDifferentToken(final String targetDeviceToken){
        return !this.deviceToken.equals(targetDeviceToken);
    }

    public void updateDeviceToken(final String newDeviceToken){
        this.deviceToken = newDeviceToken;
    }

}
