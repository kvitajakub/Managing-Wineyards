CREATE TABLE Wine_grower (
	id INTEGER PRIMARY KEY,
	name VARCHAR(20),
	surname VARCHAR(20),
	address VARCHAR(40)
	);

CREATE TABLE Wine_type (
	id INTEGER PRIMARY KEY,
	name VARCHAR(30),
	description VARCHAR(200),
	colour VARCHAR(20) CHECK( colour IN (
		'red',
		'white',
		'pink')),
	photo ORDSYS.ORDIMAGE,
	photo_si ORDSYS.SI_StillImage,
	photo_ac ORDSYS.SI_AverageColor,
	photo_ch ORDSYS.SI_ColorHistogram,
	photo_pc ORDSYS.SI_PositionalColor,
	photo_tx ORDSYS.SI_Texture
	);

CREATE TABLE Spatial_object (
	id INTEGER NOT NULL,
	valid_from INTEGER,
	valid_to INTEGER,
	object_type VARCHAR(20) CHECK( object_type IN (
		'wineyard',
		'scarecrow', 
		'wine_row',
		'road',
		'pond')),
	geometry SDO_GEOMETRY,
	wine_grower_id INTEGER,
	wine_type_id INTEGER,
	CONSTRAINT spatial_object_pk PRIMARY KEY (id, valid_from, valid_to),
	FOREIGN KEY (wine_grower_id) REFERENCES wine_grower(id) ON DELETE SET NULL,
	FOREIGN KEY (wine_type_id) REFERENCES wine_type(id) ON DELETE SET NULL,
	CONSTRAINT time_chceck1 CHECK (valid_from <= valid_to)
	);

	
INSERT INTO USER_SDO_GEOM_METADATA 
     VALUES (
     'Spatial_object',
     'geometry',
     SDO_DIM_ARRAY(
     	SDO_DIM_ELEMENT('x', 0, 600, 0.005),
     	SDO_DIM_ELEMENT('y', 0, 650, 0.005)
     	),
     NULL);


CREATE SEQUENCE Wine_grower_seq
	START WITH 1000
	INCREMENT BY 1
	NOCACHE;

CREATE SEQUENCE Wine_type_seq
	START WITH 1100
	INCREMENT BY 1
	NOCACHE;

CREATE SEQUENCE Spatial_object_seq
	START WITH 1100
	INCREMENT BY 1
	CACHE 100;

-- Insert into Wine_type
INSERT INTO Wine_type VALUES (1000,
  'Rulandské šedé',
  'Vysokokvalitné víno z obchodu.', 
  'white',
  ORDSYS.ORDIMAGE.INIT(),
  NULL,
  NULL,
  NULL,
  NULL,
  NULL);
  
INSERT INTO Wine_type VALUES (1001,
  'Merlot',
  'Tradičný zástupca pálavských vín',
  'red',
  ORDSYS.ORDIMAGE.INIT(),
  NULL,
  NULL,
  NULL,
  NULL,
  NULL);

INSERT INTO Wine_type VALUES (1002,
  'Červený tramín',
  'Svojou chuťou prevyšuje všetky ostatné',
  'white',
  ORDSYS.ORDIMAGE.INIT(),
  NULL,
  NULL,
  NULL,
  NULL,
  NULL);

INSERT INTO Wine_type VALUES (1003,
  'Cabernet Sauvignon',
  'Polosuché víno so špecifickou chuťou',
  'red',
  ORDSYS.ORDIMAGE.INIT(),
  NULL,
  NULL,
  NULL,
  NULL,
  NULL);

INSERT INTO Wine_type VALUES (1004,
  'Muškát',
  'Sladké víno s prekvapivo jemnou chuťou',
  'white',
  ORDSYS.ORDIMAGE.INIT(),
  NULL,
  NULL,
  NULL,
  NULL,
  NULL);

-- Insert into Wine_grower
INSERT INTO Wine_grower VALUES (1, 'Michal', 'Záhradník', 'Slavkov u Brna');
INSERT INTO Wine_grower VALUES (2, 'Tomáš', 'Slovák', 'Pálava');
INSERT INTO Wine_grower VALUES (3, 'Šárka', 'Nezabloudilová', 'Stratená');

--------------------------------------------------------------------------------
-- Triggers for constraints
--------------------------------------------------------------------------------
-- CREATE OR REPLACE TRIGGER before_update
-- BEFORE UPDATE
-- 	ON Spatial_object
-- 	FOR EACH ROW
-- 	WHERE OLD.ID=NEW.ID
-- DECLARE
-- 	one_rec Spatial_object%rowtype;
-- BEGIN
-- 	dbms_output.put_line('Old From: ' || :old.valid_from);
-- 	dbms_output.put_line('New From: ' || :new.valid_from);
-- END;
-- /


-- Insert into spatial object
--------------------------------------------------------------------------------
-- Polygon 1 - wine yard 1 ----------------------------------------------------------------------
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1000, 2000, 2001, 'wineyard', 
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(96,260, 98,450, 200,613, 375,578, 273,410)),
		3,
    NULL);
INSERT INTO Spatial_object VALUES (1000, 2002, 2008, 'wineyard', 
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(96,260, 26,540, 200,613, 375,578, 273,410)),
		1,
    NULL);
INSERT INTO Spatial_object VALUES (1000, 2009, 2014, 'wineyard', 
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(96,260, 98,450, 200,613, 405,635, 273,410)),
		2,
    NULL);
INSERT INTO Spatial_object VALUES (1000, 2015, 9999, 'wineyard', 
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(96,260, 98,450, 200,613, 405,635, 273,410)),
		1,
    NULL);

--------------------------------------------------------------------------------
-- Polygon 2 - wine yard 2 ----------------------------------------------------------------------
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1001, 2000, 2005, 'wineyard',
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(113,190, 386,406,  310,97, 111,90)),
		2,
    NULL);
INSERT INTO Spatial_object VALUES (1001, 2008, 2012, 'wineyard',
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(113,190, 386,406, 310,97, 111,90)),
		1,
    NULL);
INSERT INTO Spatial_object VALUES (1001, 2013, 2015, 'wineyard',
	SDO_GEOMETRY(
		2003,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1, 1003, 1),
		SDO_ORDINATE_ARRAY(60,146, 386,406, 310,97, 111,90)),
		2,
    NULL);

--------------------------------------------------------------------------------
-- Multiline - wine rows
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1002, 2006, 2013, 'wine_row',
	SDO_GEOMETRY(
		2006,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,2,1, 5,2,1, 9,2,1, 13,2,1),
		SDO_ORDINATE_ARRAY(116,160,370,370, 126,131,353,309, 139,97,334,247, 208,99,330,195)),
		2,
    1000);
INSERT INTO Spatial_object VALUES (1002, 2015, 2017, 'wine_row',
	SDO_GEOMETRY(
		2006,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,2,1, 5,2,1, 9,2,1, 13,2,1),
		SDO_ORDINATE_ARRAY(116,160,370,370, 126,131,353,309, 139,97,334,247, 208,99,330,195)),
		2,
    1001);
INSERT INTO Spatial_object VALUES (1002, 2018, 9999, 'wine_row',
	SDO_GEOMETRY(
		2006,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,2,1, 5,2,1, 9,2,1, 13,2,1),
		SDO_ORDINATE_ARRAY(80,133,370,370, 87,97,353,309, 139,97,334,247, 208,99,330,195)),
		2,
    1002);

--------------------------------------------------------------------------------
-- Multipolygon - ponds
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1003, 2000, 2010, 'pond',
	SDO_GEOMETRY(
		2007,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,1003,1, 9,1003,1, 17,1003,1),
		SDO_ORDINATE_ARRAY( 416,332, 414,432, 500,557, 500,428, 
							372,182, 392,282, 500,388, 482,242, 
							350,102, 368,163, 480,208, 475,112)),
		NULL,
    NULL);
INSERT INTO Spatial_object VALUES (1003, 2011, 2014, 'pond',
	SDO_GEOMETRY(
		2007,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,1003,1, 9,1003,1),
		SDO_ORDINATE_ARRAY( 416,332, 414,432, 500,557, 500,428, 
							350,102, 368,163, 480,208, 475,112)),
		NULL,
    NULL);
INSERT INTO Spatial_object VALUES (1003, 2015, 2017, 'pond',
	SDO_GEOMETRY(
		2007,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,1003,1, 9,1003,1, 17,1003,1),
		SDO_ORDINATE_ARRAY( 416,332, 414,432, 500,557, 500,428, 
							372,182, 392,282, 500,388, 482,242,
							350,102, 368,163, 480,208, 475,112)),
		NULL,
    NULL);

--------------------------------------------------------------------------------
-- Multiline - road
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1004, 2000, 2005, 'road',
	SDO_GEOMETRY(
		2002,
		NULL,
		NULL,
		SDO_ELEM_INFO_ARRAY(1,2,1),
		SDO_ORDINATE_ARRAY(29,170, 214,307, 338,413, 460,640)),
		NULL,
    NULL);

--------------------------------------------------------------------------------
-- Point - scarecrow
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1005, 2000, 2014, 'scarecrow',
	SDO_GEOMETRY( 2001, NULL, SDO_POINT_TYPE(184,441,NULL), NULL, NULL),
  1,
  NULL);
  
--------------------------------------------------------------------------------
-- Point - scarecrow
--------------------------------------------------------------------------------
INSERT INTO Spatial_object VALUES (1006, 2003, 2012, 'scarecrow',
	SDO_GEOMETRY( 2001, NULL, SDO_POINT_TYPE(299,288,NULL), NULL, NULL),
  3,
  NULL);
	
CREATE INDEX Spatial_object_idx ON Spatial_object (geometry) INDEXTYPE IS MDSYS.SPATIAL_INDEX;
ALTER INDEX Spatial_object_idx REBUILD;

	
commit;

