import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class FoodDisplayable implements Displayable{
	private int callListHandle;
	private float scale;
	private float angle;
	
	
	public FoodDisplayable(final float scale1) {
		this.scale = scale1;
		this.angle = 0;
	}


	@Override
	public void draw(GL2 gl) {
		// TODO Auto-generated method stub
		gl.glCallList(this.callListHandle);
		
	}


	@Override
	public void initialize(GL2 gl) {
		// TODO Auto-generated method stub
		this.callListHandle = gl.glGenLists(1);
		gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
		
		GLU glu = new GLU();
		GLUT glut = new GLUT();
		
		gl.glPushMatrix();
		glut.glutSolidSphere(0.05, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
		
		
	}
}
	
