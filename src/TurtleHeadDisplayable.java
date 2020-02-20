import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class TurtleHeadDisplayable implements Displayable{
	private int callListHandle;
	private float scale;
	private GLUquadric qd;
	
	public TurtleHeadDisplayable(final float scale) {
		this.scale = scale;
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
		this.qd = glu.gluNewQuadric();
		GLUT glut = new GLUT();
		
		gl.glPushMatrix();
		glut.glutSolidSphere(0.125, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
		
		
	}
	

}
