package seedu.addressbook.storage.jaxb;

import seedu.addressbook.data.RMS;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.order.Order;
import seedu.addressbook.data.order.UniqueOrderList;
import seedu.addressbook.data.menu.Menu;
import seedu.addressbook.data.menu.UniqueMenuList;
import seedu.addressbook.data.member.Member;
import seedu.addressbook.data.member.UniqueMemberList;
import seedu.addressbook.data.person.Menu;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.UniquePersonList;
import seedu.addressbook.data.person.Employee;
import seedu.addressbook.data.person.UniqueEmployeeList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly adapted address book data holder class.
 */
@XmlRootElement(name = "RMS")
public class AdaptedRMS {

    @XmlElement(name = "persons")
    private List<AdaptedPerson> persons = new ArrayList<>();
    @XmlElement(name = "menus")
    private List<AdaptedMenu> menus = new ArrayList<>();
    @XmlElement(name = "members")
    private List<AdaptedMember> members = new ArrayList<>();
    @XmlElement(name = "employees")
    private List<AdaptedEmployee> employees = new ArrayList<>();
    @XmlElement(name = "orders")
    private List<AdaptedOrder> orders = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public AdaptedRMS() {}

    /**
     * Converts a given RMS into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created AdaptedRMS
     */
    /*public AdaptedRMS(RMS source) {
        persons = new ArrayList<>();
        source.getAllPersons().forEach(person -> persons.add(new AdaptedPerson(person)));
    }
*/
    public AdaptedRMS(RMS source) {
        persons = new ArrayList<>();
        menus = new ArrayList<>();
        employees = new ArrayList<>();
        members = new ArrayList<>();
        source.getAllPersons().forEach(person -> persons.add(new AdaptedPerson(person)));
        source.getAllMenus().forEach(menu -> menus.add(new AdaptedMenu(menu)));
        source.getAllEmployees().forEach(employee -> employees.add(new AdaptedEmployee(employee)));
        source.getAllMembers().forEach(member -> members.add(new AdaptedMember(member)));
        source.getAllOrders().forEach(order -> orders.add(new AdaptedOrder(order)));
    }


    /**
     * Returns true if any required field is missing.
     *
     * JAXB does not enforce (required = true) without a given XML schema.
     * Since we do most of our validation using the data class constructors, the only extra logic we need
     * is to ensure that every xml element in the document is present. JAXB sets missing elements as null,
     * so we check for that.
     */
    public boolean isAnyRequiredFieldMissing() {
        return persons.stream().anyMatch(AdaptedPerson::isAnyRequiredFieldMissing);
    }

    public boolean isAnyRequiredFieldMissingMenu() {
        return menus.stream().anyMatch(AdaptedMenu::isAnyRequiredFieldMissing);
    }


    /**
     * Converts this jaxb-friendly {@code AdaptedRMS} object into the corresponding(@code RMS} object.
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public RMS toModelType() throws IllegalValueException {
        final List<Person> personList = new ArrayList<>();
        final List<Menu> menuList = new ArrayList<>();
        final List<Employee> employeeList = new ArrayList<>();
        final List<Member> memberList = new ArrayList<>();
        final List<Order> orderList = new ArrayList<>();
        for (AdaptedPerson person : persons) {
            personList.add(person.toModelType());
        }
        
      // goes through employeeList to change it  
        for (AdaptedEmployee employee : employees) {
            employeeList.add(employee.toModelType());
        }

        for (AdaptedMenu menu : menus) {
            menuList.add(menu.toModelType());
        }

        for (AdaptedMember member : members) {
            memberList.add(member.toModelType());
        }

        for (AdaptedOrder order : orders) {
            orderList.add(order.toModelType());
        }
        return new RMS(new UniquePersonList(personList), new UniqueMenuList(menuList), new UniqueEmployeeList(employeeList), new UniqueOrderList(orderList), new UniqueMemberList(memberList));

    }
}
