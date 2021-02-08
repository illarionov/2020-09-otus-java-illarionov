package ru.x0xdc.otus.java.messagesystem.mappers;

import org.springframework.stereotype.Component;
import ru.x0xdc.otus.java.messagesystem.core.model.Phone;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.PhoneEntity;

@Component
public class PhoneToPhoneEntity implements Mapper<Phone, PhoneEntity> {
    @Override
    public PhoneEntity map(Phone item) {
        return new PhoneEntity(item.getId(), item.getNumber());
    }

    @Override
    public Phone mapFrom(PhoneEntity item) {
        return new Phone(item.getId(), item.getNumber());
    }
}
