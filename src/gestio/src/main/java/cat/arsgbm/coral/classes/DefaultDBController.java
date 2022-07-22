package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.interfaces.IInicia;


public class DefaultDBController extends DefaultController implements IInicia {

    private TipusCerca mode;

    public TipusCerca getMode() {
        return mode;
    }

    public void setMode(TipusCerca mode) {
        this.mode = mode;
    }

    //AMB AIXÒ OBLIGUEM A FER CONEXIÓ A TAULA , EL CODI PER CONECTAR AMB CLASSE ESTÀ AL CONTROLADOR DE LA VISTA,(FORMULARI), QUE OBRIM
    @Override
    public void inicia() {

    }
}
