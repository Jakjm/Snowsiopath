package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * @author jordan
 *
 */
public class KeyHandler implements KeyListener {
	public boolean rightKey,leftKey,spaceKey,fKey,eKey,qKey,rKey,aKey,dKey, xKey,zKey;
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			spaceKey = true;	
		}
		else if(e.getKeyCode() == KeyEvent.VK_F) {
			fKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_E) {
			eKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_Q) {
			qKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R) {
			rKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_A) {
			aKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_D) {
			dKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_X) {
			xKey = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_Z) {
			zKey = true;
		}
		
	}
	public void onWKeyPressed() {
		
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			spaceKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_F) {
			fKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_E) {
			eKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_Q) {
			qKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_R) {
			rKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_W) {
			onWKeyPressed();
		}
		else if(e.getKeyCode() == KeyEvent.VK_A) {
			aKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_D) {
			dKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_X) {
			xKey = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_Z) {
			zKey = false;
		}
		
	}
}
