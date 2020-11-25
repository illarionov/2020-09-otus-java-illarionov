package ru.otus;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    @DisplayName("Все поля объекта инициализируются корректно")
    void builderShouldInitAllFieldsCorrectly() {
        Message message = new Message.Builder(42)
                .field1("1")
                .field2("2")
                .field3("3")
                .field4("4")
                .field5("5")
                .field6("6")
                .field7("7")
                .field8("8")
                .field9("9")
                .field10("10")
                .field11("11")
                .field12("12")
                .field13(new ObjectForMessage(List.of("13", "14", "15")))
                .build();

        Assertions.assertThat(message)
                .returns(42L, from(Message::getId))
                .returns("1", from(Message::getField1))
                .returns("2", from(Message::getField2))
                .returns("3", from(Message::getField3))
                .returns("4", from(Message::getField4))
                .returns("5", from(Message::getField5))
                .returns("6", from(Message::getField6))
                .returns("7", from(Message::getField7))
                .returns("8", from(Message::getField8))
                .returns("9", from(Message::getField9))
                .returns("10", from(Message::getField10))
                .returns("11", from(Message::getField11))
                .returns("12", from(Message::getField12))
                .returns(List.of("13", "14", "15"), from(m -> m.getField13().getData()));
    }

    @Test
    @DisplayName("toBuilder() инициализирует все поля корректно")
    void toBuilderShouldInitFieldsCorrectly() {
        Message message = new Message.Builder(42)
                .field1("1")
                .field2("2")
                .field3("3")
                .field4("4")
                .field5("5")
                .field6("6")
                .field7("7")
                .field8("8")
                .field9("9")
                .field10("10")
                .field11("11")
                .field12("12")
                .field13(new ObjectForMessage(List.of("13", "14", "15")))
                .build();

        Message newMessage = message.toBuilder().build();
        Assertions.assertThat(newMessage)
                .returns(42L, from(Message::getId))
                .returns("1", from(Message::getField1))
                .returns("2", from(Message::getField2))
                .returns("3", from(Message::getField3))
                .returns("4", from(Message::getField4))
                .returns("5", from(Message::getField5))
                .returns("6", from(Message::getField6))
                .returns("7", from(Message::getField7))
                .returns("8", from(Message::getField8))
                .returns("9", from(Message::getField9))
                .returns("10", from(Message::getField10))
                .returns("11", from(Message::getField11))
                .returns("12", from(Message::getField12))
                .returns(List.of("13", "14", "15"), from(m -> m.getField13().getData()));
    }

    @Test
    @DisplayName("Изменение объекта, полученного из getField13() не должно модифицировать исходный message")
    void field13ResultModificationShouldNotChangeMessage() {
        Message message = new Message.Builder(1)
                .field13(new ObjectForMessage(List.of("1", "2", "3")))
                .build();

        ObjectForMessage ofm = message.getField13();
        ofm.setData(List.of("0"));

        assertThat(message.getField13().getData())
                .containsExactly("1", "2", "3");
    }

    @Test
    @DisplayName("Изменение объекта, установленного через Builder.field13() не должно модифицировать результирующий message")
    void setField13ModificationShouldNotChangeMessage() {
        ObjectForMessage ofm = new ObjectForMessage(List.of("1", "2", "3"));
        Message message = new Message.Builder(1)
                .field13(ofm)
                .build();

        ofm.setData(List.of("1"));

        assertThat(message.getField13().getData())
                .containsExactly("1", "2", "3");
    }

    @Test
    void equalsHashCodeShouldWork() {
        Message message1 = new Message.Builder(42)
                .field11("11")
                .field12("12")
                .field13(new ObjectForMessage(List.of("13", "14", "15")))
                .build();

        Message message2 = new Message.Builder(42)
                .field11("16")
                .field12("17")
                .field13(new ObjectForMessage(List.of("18", "19", "20")))
                .build();
        Message message3 = new Message.Builder(42).build();
        Message messageOther = new Message.Builder(43).build();

        assertTrue(message1.equals(message1));
        assertEquals(message1, message2);
        assertEquals(message2, message1);
        assertEquals(message1.hashCode(), message1.hashCode());

        assertEquals(message2, message3);
        assertEquals(message1, message3);
        assertEquals(message1.hashCode(), message3.hashCode());

        assertNotEquals(message1, messageOther);
        assertNotEquals(message1, null);
        assertNotEquals(null, message1);
        assertNotEquals(message1, "string");
    }

    @Test
    void testToString() {
        Message message = new Message.Builder(42)
                .field10("10")
                .field11("11")
                .field12("12")
                .field13(new ObjectForMessage(List.of("13", "14", "15")))
                .build();

        assertThat(message.toString())
                .contains("field10='10'")
                .contains("field11='11'")
                .contains("field11='11'")
                .contains("field12='12'")
                .containsPattern("field13=.*\\[13, 14, 15]");
    }
}