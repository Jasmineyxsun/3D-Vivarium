import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class TurtleLegDisplayable implements Displayable{
	private int callListHandle;
	private float scale;
	private boolean left;
	private GLUquadric qd;
	
	public TurtleLegDisplayable(final float scale, boolean left) {
		this.scale = scale;
		this.left = left;
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
		if (left){
			gl.glTranslated(0, 0, -0.02f);
			gl.glRotated(30, 0, 1, 0);
			gl.glTranslated(0, 0, 0.02f);
		}else {
			gl.glTranslated(0, 0, -0.02f);
			gl.glRotated(-30, 0, 1, 0);
			gl.glTranslated(0, 0, 0.02f);
		}

		gl.glScaled(0.5, 0.3,1.8);
		glut.glutSolidSphere(0.2, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
		
		
	}
}
