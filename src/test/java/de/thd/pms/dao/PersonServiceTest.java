package de.thd.pms.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import de.thd.pms.model.Person;
import de.thd.pms.service.PersonService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration("/test-application-context.xml")
public class PersonServiceTest extends AbstractDataAccessTest {
	private PersonService personService;
	private String[] tables = {"tbl_person"};

	@Autowired
	public void setPersonDao(PersonService personService) {
		this.personService = personService;
	}

	@Test
	public void testFindById() {
		// delete all rows from db table
		deleteFromTables(tables);
		// create new line in table
		personService.create("Uno", "First", "111");
		personService.create("Due", "Second", "222");
		Iterable<Person> alleRuderer = personService.findAll();
		for (Person p : alleRuderer) {
			Long id = p.getId();
			Person found = personService.findById(id);
			assertEquals(p.getNachname(), found.getNachname());
		}
		// delete all rows from db table
		deleteFromTables(tables);
	}

	@Test
	public void testFindAll() {
		// already tested by testFindById()
		testFindById();
	}

	@Test
	public void testCreate() {
		// already tested by testFindById()
		testFindById();
	}

}
