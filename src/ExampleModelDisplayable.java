import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class ExampleModelDisplayable implements Displayable{

	private int callListHandle;
	private float scale;
	private GLUquadric qd;
	
	public ExampleModelDisplayable(final float scale) {
		this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.callListHandle);
	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {
		this.callListHandle = gl.glGenLists(1);
		gl.glNewList(this.callListHandle, GL2.GL_COMPILE);
		
		GLU glu = new GLU();
		this.qd = glu.gluNewQuadric();
		GLUT glut = new GLUT();
		
		gl.glPushMatrix();
		
//		gl.glTranslated(1, 1, 1);
//		gl.glRotated(30, 0, 0, 1);
//		gl.glTranslated(0, 0.3, 0);
//		
//		gl.glPushMatrix();
//		gl.glRotated(90, 0, 1, 0);
//		glut.glutSolidCylinder(0.3, 1, 36, 18);
//		gl.glPopMatrix();
//		
//		gl.glPushMatrix();
//		gl.glRotated(90, 0, 1, 0);
//		glut.glutSolidCylinder(0.3, 1, 36, 18);
//		gl.glPopMatrix();
//		
//		gl.glTranslated(1, 0.3, 0);
//		gl.glRotated(30, 0, 0, 1);
//		gl.glTranslated(0, -0.3, 0);
//		
//		gl.glPushMatrix();
//		gl.glRotated(90, 0, 1, 0);
//		glut.glutSolidCylinder(0.3, 1, 36, 18);
		
		glut.glutSolidTeapot(scale);
	    
		gl.glPopMatrix();

		gl.glEndList();
	}

}
