package ru.x0xdc.otus.java.messagesystem.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.x0xdc.otus.java.messagesystem.core.model.Address;
import ru.x0xdc.otus.java.messagesystem.core.model.Phone;
import ru.x0xdc.otus.java.messagesystem.core.model.User;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.AddressEntity;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.PhoneEntity;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;

import java.util.stream.Collectors;

@Component
public class UserToUserEntity implements Mapper<User, UserEntity> {

    private final Mapper<Address, AddressEntity> addressMapper;

    private final Mapper<Phone, PhoneEntity> phoneMapper;

    @Autowired
    public UserToUserEntity(Mapper<Address, AddressEntity> addressMapper, Mapper<Phone, PhoneEntity> phoneMapper) {
        this.addressMapper = addressMapper;
        this.phoneMapper = phoneMapper;
    }

    @Override
    public UserEntity map(User user) {
        return UserEntity
                .builder()
                .setId(user.getId())
                .setLogin(user.getLogin())
                .setPassword(user.getPassword())
                .setRole(user.getRole())
                .setName(user.getName())
                .setAge(user.getAge())
                .setAddress(user.getAddress() != null ? addressMapper.map(user.getAddress()) : null)
                .setPhones(user.getPhones()
                        .stream()
                        .map(phoneMapper::map)
                        .collect(Collectors.toUnmodifiableList()))
                .build();
    }

    @Override
    public User mapFrom(UserEntity user) {
        return User.builder()
                .setId(user.getId())
                .setLogin(user.getLogin())
                .setPassword(user.getPassword())
                .setRole(user.getRole())
                .setName(user.getName())
                .setAge(user.getAge())
                .setAddress(user.getAddress() != null ? addressMapper.mapFrom(user.getAddress()) : null)
                .setPhones(user.getPhones()
                        .stream()
                        .map(phoneMapper::mapFrom)
                        .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
