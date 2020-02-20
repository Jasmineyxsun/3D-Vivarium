import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class FishTailDisplayable implements Displayable{
	private int callListHandle;
	private float scale;
	private GLUquadric qd;
	
	public FishTailDisplayable(final float scale) {
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
		gl.glScaled(1.2,1.3,0.4);
		gl.glTranslatef(0.05f, 0, 0);
		gl.glRotated(-90, 0,1,0);
		gl.glTranslatef(-0.05f, 0, 0);
		glut.glutSolidCone(0.09, 0.1, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
		
	}

}