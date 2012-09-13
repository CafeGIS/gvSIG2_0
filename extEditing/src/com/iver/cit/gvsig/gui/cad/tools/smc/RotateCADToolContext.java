
//
// Vicente Caballero Navarro


package com.iver.cit.gvsig.gui.cad.tools.smc;

import com.iver.cit.gvsig.gui.cad.tools.RotateCADTool;
import java.awt.event.InputEvent;
import com.iver.andami.PluginServices;

public final class RotateCADToolContext
    extends statemap.FSMContext
{
//---------------------------------------------------------------
// Member methods.
//

    public RotateCADToolContext(RotateCADTool owner)
    {
        super();

        _owner = owner;
        setState(Rotate.PointMain);
        Rotate.PointMain.Entry(this);
    }

    public void addOption(String s)
    {
        _transition = "addOption";
        getState().addOption(this, s);
        _transition = "";
        return;
    }

    public void addPoint(double pointX, double pointY, InputEvent event)
    {
        _transition = "addPoint";
        getState().addPoint(this, pointX, pointY, event);
        _transition = "";
        return;
    }

    public void addValue(double d)
    {
        _transition = "addValue";
        getState().addValue(this, d);
        _transition = "";
        return;
    }

    public RotateCADToolState getState()
        throws statemap.StateUndefinedException
    {
        if (_state == null)
        {
            throw(
                new statemap.StateUndefinedException());
        }

        return ((RotateCADToolState) _state);
    }

    protected RotateCADTool getOwner()
    {
        return (_owner);
    }

//---------------------------------------------------------------
// Member data.
//

    transient private RotateCADTool _owner;

//---------------------------------------------------------------
// Inner classes.
//

    public static abstract class RotateCADToolState
        extends statemap.State
    {
    //-----------------------------------------------------------
    // Member methods.
    //

        protected RotateCADToolState(String name, int id)
        {
            super (name, id);
        }

        protected void Entry(RotateCADToolContext context) {}
        protected void Exit(RotateCADToolContext context) {}

        protected void addOption(RotateCADToolContext context, String s)
        {
            Default(context);
        }

        protected void addPoint(RotateCADToolContext context, double pointX, double pointY, InputEvent event)
        {
            Default(context);
        }

        protected void addValue(RotateCADToolContext context, double d)
        {
            Default(context);
        }

        protected void Default(RotateCADToolContext context)
        {
            throw (
                new statemap.TransitionUndefinedException(
                    "State: " +
                    context.getState().getName() +
                    ", Transition: " +
                    context.getTransition()));
        }

    //-----------------------------------------------------------
    // Member data.
    //
    }

    /* package */ static abstract class Rotate
    {
    //-----------------------------------------------------------
    // Member methods.
    //

    //-----------------------------------------------------------
    // Member data.
    //

        //-------------------------------------------------------
        // Statics.
        //
        /* package */ static Rotate_Default.Rotate_PointMain PointMain;
        /* package */ static Rotate_Default.Rotate_AngleOrPoint AngleOrPoint;
        private static Rotate_Default Default;

        static
        {
            PointMain = new Rotate_Default.Rotate_PointMain("Rotate.PointMain", 0);
            AngleOrPoint = new Rotate_Default.Rotate_AngleOrPoint("Rotate.AngleOrPoint", 1);
            Default = new Rotate_Default("Rotate.Default", -1);
        }

    }

    protected static class Rotate_Default
        extends RotateCADToolState
    {
    //-----------------------------------------------------------
    // Member methods.
    //

        protected Rotate_Default(String name, int id)
        {
            super (name, id);
        }

        protected void addOption(RotateCADToolContext context, String s)
        {
            RotateCADTool ctxt = context.getOwner();

            if (s.equals(PluginServices.getText(this,"cancel")))
            {
                boolean loopbackFlag =
                    context.getState().getName().equals(
                        Rotate.PointMain.getName());

                if (loopbackFlag == false)
                {
                    (context.getState()).Exit(context);
                }

                context.clearState();
                try
                {
                    ctxt.end();
                }
                finally
                {
                    context.setState(Rotate.PointMain);

                    if (loopbackFlag == false)
                    {
                        (context.getState()).Entry(context);
                    }

                }
            }
            else
            {
                boolean loopbackFlag =
                    context.getState().getName().equals(
                        Rotate.PointMain.getName());

                if (loopbackFlag == false)
                {
                    (context.getState()).Exit(context);
                }

                context.clearState();
                try
                {
                    ctxt.throwOptionException(PluginServices.getText(this,"incorrect_option"), s);
                }
                finally
                {
                    context.setState(Rotate.PointMain);

                    if (loopbackFlag == false)
                    {
                        (context.getState()).Entry(context);
                    }

                }
            }

            return;
        }

        protected void addValue(RotateCADToolContext context, double d)
        {
            RotateCADTool ctxt = context.getOwner();

            boolean loopbackFlag =
                context.getState().getName().equals(
                    Rotate.PointMain.getName());

            if (loopbackFlag == false)
            {
                (context.getState()).Exit(context);
            }

            context.clearState();
            try
            {
                ctxt.throwValueException(PluginServices.getText(this,"incorrect_value"), d);
            }
            finally
            {
                context.setState(Rotate.PointMain);

                if (loopbackFlag == false)
                {
                    (context.getState()).Entry(context);
                }

            }
            return;
        }

        protected void addPoint(RotateCADToolContext context, double pointX, double pointY, InputEvent event)
        {
            RotateCADTool ctxt = context.getOwner();

            boolean loopbackFlag =
                context.getState().getName().equals(
                    Rotate.PointMain.getName());

            if (loopbackFlag == false)
            {
                (context.getState()).Exit(context);
            }

            context.clearState();
            try
            {
                ctxt.throwPointException(PluginServices.getText(this,"incorrect_point"), pointX, pointY);
            }
            finally
            {
                context.setState(Rotate.PointMain);

                if (loopbackFlag == false)
                {
                    (context.getState()).Entry(context);
                }

            }
            return;
        }

    //-----------------------------------------------------------
    // Inner classse.
    //


        private static final class Rotate_PointMain
            extends Rotate_Default
        {
        //-------------------------------------------------------
        // Member methods.
        //

            private Rotate_PointMain(String name, int id)
            {
                super (name, id);
            }

            protected void Entry(RotateCADToolContext context)
            {
                RotateCADTool ctxt = context.getOwner();

                ctxt.selection();
                ctxt.setQuestion(PluginServices.getText(this,"insert_basis_point"));
                ctxt.setDescription(new String[]{"cancel"});
                return;
            }

            protected void addPoint(RotateCADToolContext context, double pointX, double pointY, InputEvent event)
            {
                RotateCADTool ctxt = context.getOwner();


                (context.getState()).Exit(context);
                context.clearState();
                try
                {
                    ctxt.setQuestion(PluginServices.getText(this,"insert_rotation_angle"));
                    ctxt.setDescription(new String[]{"cancel"});
                    ctxt.addPoint(pointX, pointY, event);
                }
                finally
                {
                    context.setState(Rotate.AngleOrPoint);
                    (context.getState()).Entry(context);
                }
                return;
            }

        //-------------------------------------------------------
        // Member data.
        //
        }

        private static final class Rotate_AngleOrPoint
            extends Rotate_Default
        {
        //-------------------------------------------------------
        // Member methods.
        //

            private Rotate_AngleOrPoint(String name, int id)
            {
                super (name, id);
            }

            protected void addPoint(RotateCADToolContext context, double pointX, double pointY, InputEvent event)
            {
                RotateCADTool ctxt = context.getOwner();


                (context.getState()).Exit(context);
                context.clearState();
                try
                {
                    ctxt.setDescription(new String[]{"cancel"});
                    ctxt.addPoint(pointX, pointY, event);
                    ctxt.end();
                    ctxt.refresh();
                }
                finally
                {
                    context.setState(Rotate.PointMain);
                    (context.getState()).Entry(context);
                }
                return;
            }

            protected void addValue(RotateCADToolContext context, double d)
            {
                RotateCADTool ctxt = context.getOwner();


                (context.getState()).Exit(context);
                context.clearState();
                try
                {
                    ctxt.setDescription(new String[]{"cancel"});
                    ctxt.addValue(d);
                    ctxt.end();
                    ctxt.refresh();
                }
                finally
                {
                    context.setState(Rotate.PointMain);
                    (context.getState()).Entry(context);
                }
                return;
            }

        //-------------------------------------------------------
        // Member data.
        //
        }

    //-----------------------------------------------------------
    // Member data.
    //
    }
}
