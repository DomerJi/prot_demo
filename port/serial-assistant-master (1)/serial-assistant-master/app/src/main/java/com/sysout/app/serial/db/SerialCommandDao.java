package com.sysout.app.serial.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sysout.app.serial.entity.SerialCommand;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SERIAL_COMMAND".
*/
public class SerialCommandDao extends AbstractDao<SerialCommand, Long> {

    public static final String TABLENAME = "SERIAL_COMMAND";

    /**
     * Properties of entity SerialCommand.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Remarks = new Property(1, String.class, "remarks", false, "REMARKS");
        public final static Property Command = new Property(2, String.class, "command", false, "COMMAND");
        public final static Property Type = new Property(3, Integer.class, "type", false, "TYPE");
    }


    public SerialCommandDao(DaoConfig config) {
        super(config);
    }
    
    public SerialCommandDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SERIAL_COMMAND\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"REMARKS\" TEXT," + // 1: remarks
                "\"COMMAND\" TEXT," + // 2: command
                "\"TYPE\" INTEGER);"); // 3: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SERIAL_COMMAND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SerialCommand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String remarks = entity.getRemarks();
        if (remarks != null) {
            stmt.bindString(2, remarks);
        }
 
        String command = entity.getCommand();
        if (command != null) {
            stmt.bindString(3, command);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SerialCommand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String remarks = entity.getRemarks();
        if (remarks != null) {
            stmt.bindString(2, remarks);
        }
 
        String command = entity.getCommand();
        if (command != null) {
            stmt.bindString(3, command);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SerialCommand readEntity(Cursor cursor, int offset) {
        SerialCommand entity = new SerialCommand( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // remarks
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // command
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SerialCommand entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRemarks(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCommand(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SerialCommand entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SerialCommand entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SerialCommand entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
