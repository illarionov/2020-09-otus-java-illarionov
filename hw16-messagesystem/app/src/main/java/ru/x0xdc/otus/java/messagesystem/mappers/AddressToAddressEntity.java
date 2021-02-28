package ru.x0xdc.otus.java.messagesystem.mappers;

import org.springframework.stereotype.Component;
import ru.x0xdc.otus.java.messagesystem.core.model.Address;
import ru.x0xdc.otus.java.messagesystem.hibernate.model.AddressEntity;

@Component
public class AddressToAddressEntity implements Mapper<Address, AddressEntity> {
    @Override
    public AddressEntity map(Address arg) {
        return new AddressEntity(arg.getId(), arg.getStreet());
    }

    @Override
    public Address mapFrom(AddressEntity arg) {
        return new Address(arg.getId(), arg.getStreet());
    }
}
