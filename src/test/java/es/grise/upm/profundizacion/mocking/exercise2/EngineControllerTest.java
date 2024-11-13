package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;

// @RunWith attaches a runner with the test class to initialize the test data
@RunWith(MockitoJUnitRunner.class)
public class EngineControllerTest {

	//@Mock annotation is used to create the mock object to be injected
	@Mock
	Logger logger;

	@Mock
	Speedometer speedometer;

	@Mock
	Gearbox gearbox;

	Time time = new Time();

	//@InjectMocks annotation is used to create and inject the mock object
	@InjectMocks
	EngineController engineController = new EngineController(logger, speedometer, gearbox, time);

	// Check log message format of method recordGear
	@Test
	public void testRecordGearLog(){
		Time time = mock(Time.class);
		//add the behavior of time
		when(time.getCurrentTime()).thenReturn(new Timestamp(0));
		//add the behavior of speedometer
		when(speedometer.getSpeed()).thenReturn(10.00).thenReturn(20.00).thenReturn(30.00);

		EngineController engineController = spy(new EngineController(logger, speedometer, gearbox, time));
		engineController.adjustGear();

		verify(logger).log("1970-01-01 01:00:00 Gear changed to FIRST");
	}

	// Check calculation of instantaneous speed
	@Test
	public void testAdjustGearGetInstantaneousSpeed(){
		//add the behavior of speedometer
		when(speedometer.getSpeed()).thenReturn(10.00).thenReturn(20.00).thenReturn(30.00);

		//test the getInstantaneousSpeed() functionality
		Assert.assertEquals(engineController.getInstantaneousSpeed(),20.0,0);
	}

	// Check adjustGear() calls getInstantaneousSpeed() exactly once
	@Test
	public void testAdjustGearGetInstantaneousSpeedCalls(){
		EngineController engineController = spy(new EngineController(logger, speedometer, gearbox, time));
		engineController.adjustGear();

		verify(engineController, times(1)).getInstantaneousSpeed();
	}

	// Check adjustGear() calls getSpeed() exactly three times
	@Test
	public void testAdjustGearGetSpeedCalls(){
		EngineController engineController = spy(new EngineController(logger, speedometer, gearbox, time));
		engineController.adjustGear();

		verify(speedometer, times(3)).getSpeed();
	}

	// Check adjustGear() records new gear
	@Test
	public void testAdjustGearRecordGear(){
		//add the behavior of speedometer
		when(speedometer.getSpeed()).thenReturn(10.00).thenReturn(20.00).thenReturn(30.00);

		EngineController engineController = spy(new EngineController(logger, speedometer, gearbox, time));
		engineController.adjustGear();

		verify(engineController).recordGear(GearValues.FIRST);
	}

	// Check adjustGear() sets new gear
	@Test
	public void testAdjustGearSetGear(){
		//add the behavior of speedometer
		when(speedometer.getSpeed()).thenReturn(10.00).thenReturn(20.00).thenReturn(30.00);

		EngineController engineController = spy(new EngineController(logger, speedometer, gearbox, time));
		engineController.adjustGear();

		verify(engineController).setGear(GearValues.FIRST);
	}

}
