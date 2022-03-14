package de.thd.pms.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import de.thd.pms.dto.FahrtBootPersonDTO;
import de.thd.pms.dto.FahrtPersonenDTO;
import de.thd.pms.model.Boot;
import de.thd.pms.model.Fahrt;
import de.thd.pms.model.Person;
import de.thd.pms.service.BootService;
import de.thd.pms.service.DaoException;
import de.thd.pms.service.FahrtService;
import de.thd.pms.service.PersonService;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration("/test-application-context.xml")
public class FahrtServiceTest extends AbstractDataAccessTest {
	private FahrtService fahrtService;
	private PersonService personService;
	private BootService bootService;
	private String[] tables = {"tbl_fahrt", "tbl_boot", "tbl_person"};

	@Autowired
	public void setFahrtDao(FahrtService fahrtService) {
		this.fahrtService = fahrtService;
	}

	@Autowired
	public void setPersonDao(PersonService personService) {
		this.personService = personService;
	}

	@Autowired
	public void setBootDao(BootService bootService) {
		this.bootService = bootService;
	}

	@BeforeEach
	public void before() {
		try {
			// create two persons
			personService.create("Uno", "First", "111");
			personService.create("Due", "Second", "222");
			// store them in a set
			Iterable<Person> rowers = personService.findAll();
			List<Long> sitze = new LinkedList<>();
			for (Person p : rowers) {
				sitze.add(p.getId());
			}
			// create a boat
			bootService.create("Two", 2, "Zweier");
			// retrieve it from db
			for (Boot b : bootService.findAll()) {
				fahrtService.beginne(b.getId(), sitze.toArray(new Long[sitze.size()]));
			}
		} catch (DaoException e) {
			fail("Could not create objects.");
		}
	}
	
	@AfterEach
	public void after() {
		try {
			deleteFromTables(tables);
		} catch (DataIntegrityViolationException e) {
			// Nothing to do: this may happen when the database is already empty. 
		}
	}
	
	@Test
	public void testFindNichtBeendetDTO() throws DaoException {
		Set<FahrtPersonenDTO> fahrten = fahrtService.findNichtBeendetDTO();
		assertEquals(1, fahrten.size());
	}

	@Test
	public void testFindAllDTO() throws DaoException {
		Set<FahrtPersonenDTO> fahrten = fahrtService.findAllDTO();
		assertEquals(1, fahrten.size());
	}

	@Test
	public void testFindAll() throws DaoException {
		Iterable<Fahrt> fahrten = fahrtService.findAll();
		assertTrue(fahrten.iterator().hasNext());
	}

	@Test
	public void testBeginne() throws DaoException {
		Long bootId = 0L;
		for (Boot b : bootService.findAll()) {
			bootId = b.getId();
		}
		Iterable<Person> anzahlPersonen = personService.findAll();
		List<Long> personenIds = new LinkedList<>();
		for (Person p : anzahlPersonen) {
			personenIds.add(p.getId());
		}
		try {
			fahrtService.beginne(bootId, personenIds.toArray(new Long[personenIds.size()]));
		} catch (DaoException e) {
			fail(e.getMessage());
		}
		// expect 2 - one created in createEverything, the other in beginne
		Iterable<Fahrt> fahrten = fahrtService.findAll();
		assertEquals(2, fahrten.spliterator().getExactSizeIfKnown());
	}

	@Test
	public void testBeende() throws DaoException {
		for (Fahrt f : fahrtService.findAll()) {
			fahrtService.beende(f.getId());
		}
		for (Fahrt f : fahrtService.findAll()) {
			assertNotNull(f.getAnkunft());
		}
		
	}

	@Test
	public void testFindAllVoll() throws DaoException {
		List<FahrtBootPersonDTO> fahrten = fahrtService.findAllVoll();
		// expect two records!
		assertEquals(2, fahrten.size());
	}

}
