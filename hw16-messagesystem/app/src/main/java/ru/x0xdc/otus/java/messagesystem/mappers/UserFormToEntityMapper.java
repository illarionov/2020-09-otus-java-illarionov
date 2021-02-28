package ru.x0xdc.otus.java.messagesystem.mappers;

import org.springframework.stereotype.Component;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.PhoneEntity;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntity;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.UserEntityBuilder;
import ru.x0xdc.otus.java.messagesystem.messagesystem.dto.UserForm;

import java.util.stream.Collectors;

@Component
public class UserFormToEntityMapper implements Mapper<UserForm, UserEntity> {

    @Override
    public UserEntity map(UserForm form) {
        UserEntityBuilder newUserBuilder = UserEntity.builder()
                .setLogin(form.getLogin())
                .setPassword(form.getPassword())
                .setRole(form.getRole())
                .setAge(form.getAge())
                .setName(form.getName());

        if (!form.getAddress().isEmpty()) {
            newUserBuilder.setAddress(form.getAddress());
        }

        if (!form.getPhone().isEmpty()) {
            newUserBuilder.addPhone(form.getPhone());
        }

        return newUserBuilder.build();
    }

    @Override
    public UserForm mapFrom(UserEntity entity) {
        UserForm userForm = new UserForm();
        userForm.setLogin(entity.getLogin());
        userForm.setPassword(entity.getPassword());
        userForm.setName(entity.getName());
        userForm.setRole(entity.getRole());
        userForm.setAge(entity.getAge());
        userForm.setAddress(entity.getAddress() != null ? entity.getAddress().getStreet() : "");
        userForm.setPhone(entity.getPhones()
                .stream()
                .map(PhoneEntity::getNumber)
                .collect(Collectors.joining("; "))
        );
        return userForm;
    }
}
