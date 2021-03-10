package Input;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

public class PlayerController implements Controller  {

	private Input input;
	
	
	public PlayerController(Input input) {
		this.input = input;
	}
	
	@Override
	public boolean isRequestingUp() {
		return input.isPressed(0);
	}

	@Override
	public boolean isRequestingDown() {
		return input.isPressed(1);
	}

	@Override
	public boolean isRequestingRight() {
		return input.isPressed(3);
	}

	@Override
	public boolean isRequestingLeft() {
		return input.isPressed(2);
	}

}
