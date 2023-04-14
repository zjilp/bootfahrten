package de.thd.pms.controller;

import java.time.LocalDateTime;

import de.thd.pms.model.Boot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import de.thd.pms.model.Person;
import de.thd.pms.service.DaoException;
import de.thd.pms.service.PersonService;

@Controller
@RequestMapping("/person")
@Tag(name = "person", description = "Die Personapi")
public class PersonController {
	private static Logger log = LogManager.getLogger(PersonController.class);
	@Autowired
	private PersonService personService;

    /**
     * <p>Controller to edit a {@link Person} object. If id == null, an empty form is presented.</p>
     * @param id of a {@link Person}
     * @return a ModelAndView object to be used by view "edit-person"
     */
    @RequestMapping(value="/edit", method=RequestMethod.GET)
    public ModelAndView edit(@RequestParam(required=false) Long id) {
    	log.debug("edit id=" + id);
    	ModelAndView mv = new ModelAndView();
    	if (id == null) {
    		mv.addObject(new Person());
    	} else { 
    		mv.addObject(personService.findById(id));
    	}
    	mv.setViewName("edit-person");
    	return mv;
    }
    
    /**
     * <p>Saves a person.</p>
     * 
     * <p>Expected HTTP POST and request '/save'.</p>
     * @return 
     */
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public String save(Person person, Model model) {
        if (person.getCreated() == null) {
            person.setCreated(LocalDateTime.now());
        }
        personService.save(person);
        model.addAttribute("statusMessageKey", "person.form.msg.success");
        return "redirect:edit?id=" + person.getId();
    }

    /**
     * <p>Deletes a person.</p>
     * 
     * <p>Expected HTTP GET and request '/delete'.</p>
     */
    @RequestMapping(value="/delete", method=RequestMethod.GET)
    public ModelAndView delete(Long id) {
        try {
			personService.delete(id);
	        return new ModelAndView("redirect:findAll");
		} catch (DaoException e) {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("message", e.getMessage());
			return mv;
		}
    }

    /**
     * <p>Searches for all persons and returns them in a 
     * <code>Collection</code>.</p>
     * 
     * <p>Expected HTTP GET and request '/findAll'.</p>
     */
    @RequestMapping(value="/findAll", method=RequestMethod.GET)
    public ModelAndView findAll() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("title", "Vereinsmitglieder");
		mv.addObject("message", "Alle Mitglieder des Vereins");
		mv.addObject("personen", personService.findAll());
		mv.setViewName("list-personen");
		return mv;
    }

	@Operation(summary = "Get a person by id")
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Person> rest_get(@PathVariable Long id) {
		return ResponseEntity.ok(personService.findById(id));
	}
}
