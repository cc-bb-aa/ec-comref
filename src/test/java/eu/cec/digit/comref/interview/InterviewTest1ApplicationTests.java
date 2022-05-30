package eu.cec.digit.comref.interview;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.cec.digit.comref.interview.persistent.domain.Watch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class InterviewTest1ApplicationTests {

	@Autowired
	private InterviewTest1Application interviewTest1Application;

	@AfterEach
	public void cleanup() {

		log.info("Cleaning up");
		interviewTest1Application.findAll().forEach(w -> interviewTest1Application.deleteWatch(w.getName()));
		List<Watch> watches = interviewTest1Application.findAll();

		assertTrue(watches.isEmpty());
	}

	@Test
	public void testBasicCrud() {

		interviewTest1Application.addWatch("Jaeger-LeCoultre", 10000, 3, true);
		interviewTest1Application.addWatch("Lange & Söhne", 20000, 2, true);
		interviewTest1Application.addWatch("Audemars Piguet", 30000, 1, false);

		Optional<Watch> watch = interviewTest1Application.getWatch("Jaeger-LeCoultre").stream().findFirst();

		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Jaeger-LeCoultre"));
			assertTrue(w.getSold().equals(3));
			assertTrue(w.getValue().equals(10000));
			assertTrue(w.getAvailable());
		});

		watch = interviewTest1Application.getWatch("Lange & Söhne").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Lange & Söhne"));
			assertTrue(w.getSold().equals(2));
			assertTrue(w.getValue().equals(20000));
			assertTrue(w.getAvailable());
		});

		watch = interviewTest1Application.getWatch("Audemars Piguet").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Audemars Piguet"));
			assertTrue(w.getSold().equals(1));
			assertTrue(w.getValue().equals(30000));
			assertFalse(w.getAvailable());
		});

		interviewTest1Application.updateWatch("Jaeger-LeCoultre", 10001, 3, true);

		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Jaeger-LeCoultre"));
			assertTrue(w.getSold().equals(3));
			assertTrue(w.getValue().equals(10001));
			assertTrue(w.getAvailable());
		});

		interviewTest1Application.deleteWatch("Jaeger-LeCoultre");
		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre").stream().findFirst();
		assertTrue(watch.isEmpty());

	}

	@Test
	public void incrementWatchSoldCount() {

		interviewTest1Application.addWatch("Jaeger-LeCoultre", 10000, 3, true);

		Optional<Watch> watch = interviewTest1Application.getWatch("Jaeger-LeCoultre").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Jaeger-LeCoultre"));
			assertTrue(w.getSold().equals(3));
			assertTrue(w.getValue().equals(10000));
			assertTrue(w.getAvailable());
		});

		interviewTest1Application.incrementWatchSales("Jaeger-LeCoultre Nouveau");
		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre Nouveau").stream().findFirst();
		watch.ifPresent(w -> assertTrue(w.getSold().equals(4)));

		interviewTest1Application.incrementWatchSales("Jaeger-LeCoultre Second");
		watch = interviewTest1Application.getWatch("Jaeger-LeCoultre Second").stream().findFirst();
		watch.ifPresent(w -> assertTrue(w.getSold().equals(5)));

	}

	@Test
	public void testWatchWithLongName() {

		interviewTest1Application.addWatch("eCRIaGRaCepYArcELpaNKLExEmeTericaMELVeRyPOnaterstr", 10000, 3, true);

		Optional<Watch> watch = interviewTest1Application.getWatch("eCRIaGRaCepYArcELpaNKLExEmeTericaMELVeRyPOnaterstr").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("eCRIaGRaCepYArcELpaNKLExEmeTericaMELVeRyPOnaterstr"));
			assertTrue(w.getSold().equals(3));
			assertTrue(w.getValue().equals(10000));
			assertTrue(w.getAvailable());
		});

	}

	@Test
	public void removeOutOfStockWatchesTest() {

		interviewTest1Application.addWatch("Jaeger-LeCoultre", 10000, 3, true);
		interviewTest1Application.addWatch("Lange & Söhne", 20000, 2, true);
		interviewTest1Application.addWatch("Audemars Piguet", 30000, 1, false);

		interviewTest1Application.removeOutOfStockWatches();

		List<Watch> watches = interviewTest1Application.findAll();
		assertTrue(watches.size() == 2);

		Optional<Watch> watch = interviewTest1Application.getWatch("Jaeger-LeCoultre").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Jaeger-LeCoultre"));
			assertTrue(w.getSold().equals(3));
			assertTrue(w.getValue().equals(10000));
			assertTrue(w.getAvailable());
		});

		watch = interviewTest1Application.getWatch("Lange & Söhne").stream().findFirst();
		watch.ifPresent(w -> {
			assertNotNull(w);
			assertTrue(w.getName().equals("Lange & Söhne"));
			assertTrue(w.getSold().equals(2));
			assertTrue(w.getValue().equals(20000));
			assertTrue(w.getAvailable());
		});

	}

	@Test
	public void addMultipleWatches() {

		List<Watch> list = new ArrayList<>();

		while (list.size() < 1000) {
			list.add(new Watch(UUID.randomUUID().toString(), 1, 1, true));

		}

		long start = System.currentTimeMillis();
		interviewTest1Application.slowAddWatches(list);
		long tookSlow = System.currentTimeMillis() - start;

		interviewTest1Application.findAll().forEach(w -> interviewTest1Application.deleteWatch(w.getName()));

		start = System.currentTimeMillis();
		interviewTest1Application.fastAddWatches(list);
		long tookFast = System.currentTimeMillis() - start;

		log.info("slow: {}, fast: {}", tookSlow, tookFast);

	}
}
