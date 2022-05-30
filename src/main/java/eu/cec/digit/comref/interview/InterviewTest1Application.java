package eu.cec.digit.comref.interview;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eu.cec.digit.comref.interview.persistent.domain.Watch;
import eu.cec.digit.comref.interview.persistent.repository.WatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootApplication
public class InterviewTest1Application implements CommandLineRunner {

	@Autowired
	private WatchRepository watchRepository;

	public static void main(String[] args) {
		SpringApplication.run(InterviewTest1Application.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Starting test one");

	}

	public void slowAddWatches(List<Watch> watches) {

		for(Watch watch : watches) {
			watchRepository.save(watch);
		}

	}

	public void fastAddWatches(List<Watch> watches) {

			watchRepository.saveAll(watches);

	}
	
	public void removeOutOfStockWatches() {

		
		List<Watch> watches = watchRepository.findAll();
		
		for(Watch watch : watches) {
			
			if(!watch.getAvailable()) {
				watchRepository.delete(watch);
			}
		}
		

	}

	public Watch addWatch(String name, Integer value, Integer sold, Boolean available) {

		Watch watch = new Watch(null, null, null, null);
		watch.setAvailable(available);
		watch.setName(name);
		watch.setSold(sold);
		watch.setValue(value);

		return watchRepository.save(watch);

	}

	public void updateWatch(String name, Integer value, Integer sold, Boolean available) {

		Optional<Watch> watch = getWatch(name).stream().findFirst();
		watch.ifPresent(w -> {
			w.setAvailable(available);
			w.setSold(sold);
			w.setValue(value);

			watchRepository.save(w);
		});
	}

	public Optional<Watch> getWatch(String name) {

		return watchRepository.findByName(name);

	}

	public void incrementWatchSales(String name) {

		boolean found = watchRepository.existsByName(name);

		if (!found) {
			Watch watch = new Watch();
			watch.setValue(watch.getValue());
			watchRepository.save(watch);
		}
	}

	public List<Watch> findAll() {

		return watchRepository.findAll();

	}

	@Transactional
	public void deleteWatch(String name) {

		watchRepository.deleteByName(name);
	}
}
