package es.grise.upm.profundizacion.mocking.exercise2;

import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

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
	Logger loggerMock;

	@Mock
	Speedometer speedometerMock;

	@Mock
	Gearbox gearboxMock;

	@Mock
	Time timeMock;

	//@InjectMocks annotation is used to create and inject the mock object
	@InjectMocks
	EngineController engineController = new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock);

	// Check log message format of method recordGear
	@Test
	public void testRecordGearLog(){
		//add the behavior of timeMock
		when(timeMock.getCurrentTime()).thenReturn(new Timestamp(0));

		engineController.recordGear(GearValues.FIRST);

		// verify log format
		String logFormat = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} Gear changed to FIRST";
		verify(loggerMock).log(matches(logFormat));

		// verify no more interactions with loggerMock
		verifyNoMoreInteractions(loggerMock);
	}

	// Check calculation of instantaneous speed
	@Test
	public void testAdjustGearGetInstantaneousSpeed(){
		//add the behavior of speedometerMock
		when(speedometerMock.getSpeed()).thenReturn(10.00, 15.00, 20.00);

		//test the getInstantaneousSpeed() functionality
		Assert.assertEquals(engineController.getInstantaneousSpeed(),15.00,0);
	}

	// Check adjustGear() calls getInstantaneousSpeed() exactly once
	@Test
	public void testAdjustGearGetInstantaneousSpeedCalls(){
		//add the behavior of timeMock
		when(timeMock.getCurrentTime()).thenReturn(new Timestamp(0));

		EngineController engineController = spy(new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock));
		engineController.adjustGear();

		// Verify number of calls
		verify(engineController, times(1)).getInstantaneousSpeed();
	}

	// Check adjustGear() calls getSpeed() exactly three timeMocks
	@Test
	public void testAdjustGearGetSpeedCalls(){
		//add the behavior of timeMock
		when(timeMock.getCurrentTime()).thenReturn(new Timestamp(0));

		engineController.adjustGear();

		// Verify number of calls
		verify(speedometerMock, times(3)).getSpeed();
	}

	// Check adjustGear() records new gear
	@Test
	public void testAdjustGearRecordGear(){
		//add the behavior of timeMock
		when(timeMock.getCurrentTime()).thenReturn(new Timestamp(0));

		//add the behavior of speedometerMock
		when(speedometerMock.getSpeed()).thenReturn(10.00, 15.00, 20.00);

		EngineController engineController = spy(new EngineController(loggerMock, speedometerMock, gearboxMock, timeMock));
		engineController.adjustGear();

		// Verify recordGear
		verify(engineController).recordGear(GearValues.FIRST);
	}

	// Check adjustGear() sets new gear
	@Test
	public void testAdjustGearSetGear(){
		//add the behavior of timeMock
		when(timeMock.getCurrentTime()).thenReturn(new Timestamp(0));

		//add the behavior of speedometerMock
		when(speedometerMock.getSpeed()).thenReturn(10.00, 15.00, 20.00);

		engineController.adjustGear();

		// Verify setGear
		verify(gearboxMock).setGear(GearValues.FIRST);
	}
}
