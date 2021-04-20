package de.thd.pms.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.thd.pms.model.Boot;
import de.thd.pms.model.Fahrt;
import de.thd.pms.repository.BootRepository;
import de.thd.pms.repository.FahrtRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


/**
 * The Data access class for {@link Boot} objects. All Interaction with the database
 * regarding the entity bean {@link Boot} should be handled by this class!
 * @author josef.schneeberger@th-deg.de
 *
 * TODO: remove classes from results, use Object[]
 *
 */
@Service
@Transactional
public class BootService {
	@Autowired
	private FahrtService fahrtService;
	@Autowired
	private BootRepository bootRepository;
	@Autowired
	private FahrtRepository fahrtRepository;
	@PersistenceContext
	private EntityManager em;

	/**
	 * Creates a new {@link Boot} and saves it in the DB.
	 * @param name
	 * @param anzahlSitze
	 * @param klasse
	 */
	public void create(String name, int anzahlSitze, String klasse) {
		Boot b = new Boot(name, klasse, anzahlSitze);
		save(b);
	}

	/**
	 * Returns a single boat by its primary db key
	 * @param id the primary key of a {@link Boot}
	 * @return a single Boot
	 */
	public Boot findById(Long id) {
		Query query = em.createNativeQuery("SELECT * FROM tbl_boot WHERE pk_boot = ?", Boot.class);
		query.setParameter(1, id);
		Boot b = (Boot)query.getSingleResult();
		return b;
	}
	
	/**
	 * Returns all boats from the database.
	 * @return a list of Boot
	 * @see Boot
	 */
	public List<Boot> findAll() {
		Query query = em.createNativeQuery("SELECT * FROM tbl_boot;");
		List<Object[]> ret = query.getResultList();
		List<Boot> boote = new LinkedList<>();
		for (Object[] o : ret) {
			Boot b = new Boot();
			b.setId(((BigInteger)o[0]).longValue());
			b.setCreated(((Timestamp)o[1]).toLocalDateTime());
			b.setKlasse((String)o[2]);
			b.setName((String)o[3]);
			b.setSitze((int)o[4]);
			boote.add(b);
		}
		return boote;
	}

	/**
	 * Saves the {@link Boot} specified by the parameter in the database.
	 * @param boot a {@link Boot} object that should be saved in the db.
	 * @return the object specified by the parameter
	 */
	// Das geht wegen @Transactional
	public Boot save(Boot boot) {

		Query query = em.createNativeQuery("UPDATE tbl_boot SET name = :name, sitze = :sitze, klasse = :klasse WHERE pk_boot = :pk");
		// Create new boot if id is 0
		if (boot.getId() == null || boot.getId() == 0) {

			Query mx = em.createNativeQuery("SELECT MAX(pk_boot) from tbl_boot");
			Object ret = mx.getSingleResult();
			long id = 1;
			if (ret != null)
				id = ((BigInteger)ret).longValue();
			boot.setId(++id);

			query = em.createNativeQuery("INSERT INTO tbl_boot(pk_boot, created, name, sitze, klasse) VALUES (:pk, :created, :name, :sitze, :klasse  );");
			query.setParameter("created", LocalDateTime.now());

		}

		query.setParameter("pk", boot.getId());
		query.setParameter("name", boot.getName());
		query.setParameter("sitze", boot.getSitze());
		query.setParameter("klasse", boot.getKlasse());

		query.executeUpdate();
		return boot;
	}

	/**
	 * Returns the list of {@link Boot} objects which are currently not on a trip.
	 * The method works as follows:
	 * <ul>
	 * <li>The Fahrt service is queried for busy boats</li>
	 * <li>Retrieve all {@link Boot} objects from the db</li>
	 * <li>Create an empty list for results and empty boats</li>
	 * <li>Intersect both lists</li>
	 * </ul>
	 * @return a List of Boot
	 */
	public List<Boot> findFreie() {
		List<Fahrt> aktuelleFahrten = fahrtRepository.findByAnkunftIsNull();
		Iterable<Boot> alle = findAll();
		List<Boot> result = new LinkedList<Boot>();
		List<Boot> belegteBoote = new LinkedList<Boot>();
		for (Fahrt fahrt : aktuelleFahrten) {
			belegteBoote.add(fahrt.getBoot());
		}
		for (Boot boot : alle) {
			if (!belegteBoote.contains(boot)) {
				result.add(boot);
			}
		}
		return result;
	}

	/**
	 * Deletes the specified {@link Boot} object from the database.
	 * @param id the primary key of the {@link Boot} to be deleted.
	 * @throws DaoException if the specified {@link Boot} is included in a {@link Fahrt}
	 */
	public void delete(Long id) throws DaoException {
		Boot boot = findById(id);
		Set<Fahrt> fahrtenVonBoot = fahrtService.findByBootId(boot.getId());
		if (fahrtenVonBoot.size() == 0) {
			Query query = em.createNativeQuery("DELETE FROM tbl_boot WHERE pk_boot = :id ;");
			query.setParameter("id", id);
			query.executeUpdate();
		} else {
			throw new DaoException("Das Boot kann nicht gelöscht werden, da bereits Fahrten mit diesem Boot durchgeführt worden sind.");
		}
	}

}