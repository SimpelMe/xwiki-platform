/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.xpn.xwiki.plugin.applicationmanager.doc;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.StringProperty;
import com.xpn.xwiki.objects.classes.BaseClass;
import com.xpn.xwiki.plugin.applicationmanager.ApplicationManagerException;
import com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.AbstractSuperClass;
import com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.SuperDocument;

import java.util.List;

/**
 * {@link com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.SuperClass}
 * implementation for XAppClasses.XWikiApplicationClass class.
 * 
 * @version $Id: $
 * @see com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.SuperClass
 * @see AbstractSuperClass
 */
public class XWikiApplicationClass extends AbstractSuperClass
{
    /**
     * Default list display type of XAppClasses.XWikiApplicationClass fields.
     */
    public static final String DEFAULT_FIELDDT = "input";

    /**
     * Default list separators of XAppClasses.XWikiApplicationClass fields.
     */
    public static final String FIELDS_DESCRIPTION = "|";

    /**
     * Name of field <code>appname</code> for the XWiki class XAppClasses.XWikiApplicationClass.
     * The name of the application.
     */
    public static final String FIELD_APPNAME = "appname";

    /**
     * Pretty name of field <code>appname</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_APPNAME = "Application Name";

    /**
     * Name of field <code>description</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass. The description of the application.
     */
    public static final String FIELD_DESCRIPTION = "description";

    /**
     * Pretty name of field <code>description</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_DESCRIPTION = "Description";

    /**
     * Name of field <code>version</code> for the XWiki class XAppClasses.XWikiApplicationClass.
     * The version of the application.
     */
    public static final String FIELD_APPVERSION = "appversion";

    /**
     * Pretty name of field <code>version</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_APPVERSION = "Application Version";

    /**
     * Name of field <code>dependencies</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass. The list of plugins on which application depends.
     */
    public static final String FIELD_DEPENDENCIES = "dependencies";

    /**
     * Pretty name of field <code>dependencies</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_DEPENDENCIES = "Dependencies";

    /**
     * Name of field <code>applications</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass. The list of other applications on which current
     * application depends.
     */
    public static final String FIELD_APPLICATIONS = "applications";

    /**
     * Pretty name of field <code>applications</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_APPLICATIONS = "Applications";

    /**
     * Name of field <code>documents</code> for the XWiki class XAppClasses.XWikiApplicationClass.
     * The list of documents application contains.
     */
    public static final String FIELD_DOCUMENTS = "documents";

    /**
     * Pretty name of field <code>documents</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_DOCUMENTS = "Documents";

    /**
     * Name of field <code>docstoinclude</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass. The list of document application contains that will be
     * included in place of copy from wiki template.
     */
    public static final String FIELD_DOCSTOINCLUDE = "docstoinclude";

    /**
     * Pretty name of field <code>docstoinclude</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_DOCSTOINCLUDE = "Documents to include";

    /**
     * Name of field <code>docstolink</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass. The list of document application contains that will be
     * linked in place of copy from wiki template.
     */
    public static final String FIELD_DOCSTOLINK = "docstolink";

    /**
     * Pretty name of field <code>docstolink</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_DOCSTOLINK = "Documents to link";

    /**
     * Name of field <code>translationdocs</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELD_TRANSLATIONDOCS = "translationdocs";

    /**
     * Pretty name of field <code>translationdocs</code> for the XWiki class
     * XAppClasses.XWikiApplicationClass.
     */
    public static final String FIELDPN_TRANSLATIONDOCS = "Translations documents";

    // ///

    /**
     * Space of class document.
     */
    private static final String CLASS_SPACE_PREFIX = "XApp";

    /**
     * Prefix of class document.
     */
    private static final String CLASS_PREFIX = "XWikiApplication";

    /**
     * The default parent page of an application descriptor document.
     */
    private static final String DEFAULT_APPLICATION_PARENT =
        CLASS_SPACE_PREFIX + "Manager.WebHome";

    /**
     * The default application version of an application descriptor document.
     */
    private static final String DEFAULT_APPLICATION_VERSION = "1.0";

    /**
     * Unique instance of XWikiApplicationClass.
     */
    private static XWikiApplicationClass instance;

    /**
     * Construct the overload of AbstractSuperClass with spaceprefix={@link #CLASS_SPACE_PREFIX}
     * and prefix={@link #CLASS_PREFIX}.
     */
    protected XWikiApplicationClass()
    {
        super(CLASS_SPACE_PREFIX, CLASS_PREFIX);
    }

    /**
     * Return unique instance of XWikiApplicationClass and update documents for this context. It
     * also check if the corresponding Xwiki class/template/sheet exist in context's database and
     * create it if not.
     * 
     * @param context the XWiki context.
     * @return a unique instance of XWikiApplicationClass.
     * @throws XWikiException error when checking for class, class template and class sheet.
     */
    public static XWikiApplicationClass getInstance(XWikiContext context) throws XWikiException
    {
        synchronized (XWikiApplicationClass.class) {
            if (instance == null) {
                instance = new XWikiApplicationClass();
            }
        }

        instance.check(context);

        return instance;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.AbstractSuperClass#updateBaseClass(com.xpn.xwiki.objects.classes.BaseClass)
     */
    protected boolean updateBaseClass(BaseClass baseClass)
    {
        boolean needsUpdate = super.updateBaseClass(baseClass);

        needsUpdate |= baseClass.addTextField(FIELD_APPNAME, FIELDPN_APPNAME, 30);
        needsUpdate |= baseClass.addTextAreaField(FIELD_DESCRIPTION, FIELDPN_DESCRIPTION, 40, 5);
        needsUpdate |= baseClass.addTextField(FIELD_APPVERSION, FIELDPN_APPVERSION, 30);

        needsUpdate |=
            baseClass.addStaticListField(FIELD_DEPENDENCIES, FIELDPN_DEPENDENCIES, 80, true, "",
                DEFAULT_FIELDDT, FIELDS_DESCRIPTION);

        needsUpdate |=
            baseClass.addStaticListField(FIELD_APPLICATIONS, FIELDPN_APPLICATIONS, 80, true, "",
                DEFAULT_FIELDDT, FIELDS_DESCRIPTION);

        needsUpdate |=
            baseClass.addStaticListField(FIELD_DOCUMENTS, FIELDPN_DOCUMENTS, 80, true, "",
                DEFAULT_FIELDDT, FIELDS_DESCRIPTION);

        needsUpdate |=
            baseClass.addStaticListField(FIELD_DOCSTOINCLUDE, FIELDPN_DOCSTOINCLUDE, 80, true,
                "", DEFAULT_FIELDDT, FIELDS_DESCRIPTION);

        needsUpdate |=
            baseClass.addStaticListField(FIELD_DOCSTOLINK, FIELDPN_DOCSTOLINK, 80, true, "",
                DEFAULT_FIELDDT, FIELDS_DESCRIPTION);

        needsUpdate |=
            baseClass.addStaticListField(FIELD_TRANSLATIONDOCS, FIELDPN_TRANSLATIONDOCS, 80,
                true, "", DEFAULT_FIELDDT, FIELDS_DESCRIPTION);

        return needsUpdate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.AbstractSuperClass#updateClassTemplateDocument(com.xpn.xwiki.doc.XWikiDocument)
     */
    protected boolean updateClassTemplateDocument(XWikiDocument doc)
    {
        boolean needsUpdate = false;

        if (!(DEFAULT_APPLICATION_PARENT).equals(doc.getParent())) {
            doc.setParent(DEFAULT_APPLICATION_PARENT);
            needsUpdate = true;
        }

        if (!DEFAULT_APPLICATION_VERSION.equals(doc.getStringValue(getClassFullName(),
            FIELD_APPVERSION))) {
            doc.setStringValue(getClassFullName(), FIELD_APPVERSION, DEFAULT_APPLICATION_VERSION);
            needsUpdate = true;
        }

        return needsUpdate;
    }

    /**
     * Get the XWiki document descriptor of containing XAppClasses.XWikiApplication XWiki object
     * with "appname" field equals to <code>appName</code>.
     * 
     * @param appName the name of the application.
     * @param context the XWiki context.
     * @param validate indicate if it return new XWikiDocument or throw exception if application
     *            descriptor does not exist.
     * @return the XWikiDocument representing application descriptor.
     * @throws XWikiException error when searching for application descriptor document.
     * @see #getApplication(String, XWikiContext, boolean)
     */
    private XWikiDocument getApplicationDocument(String appName, XWikiContext context,
        boolean validate) throws XWikiException
    {
        XWiki xwiki = context.getWiki();

        List listApp =
            searchItemDocumentsByField(FIELD_APPNAME, appName, StringProperty.class
                .getSimpleName(), context);

        if (listApp.size() == 0) {
            if (validate) {
                throw new ApplicationManagerException(
                    ApplicationManagerException.ERROR_APPLICATIONMANAGER_DOES_NOT_EXIST,
                    appName + " application does not exist");
            } else {
                return xwiki.getDocument(getItemDocumentDefaultFullName(appName, context),
                    context);
            }
        }

        return (XWikiDocument) listApp.get(0);
    }

    /**
     * Get the XWiki document descriptor of containing XAppClasses.XWikiApplication XWiki object
     * with "appname" field equals to <code>appName</code>.
     * 
     * @param appName the name of the application.
     * @param context the XWiki context.
     * @param validate indicate if it return new XWikiDocument or throw exception if application
     *            descriptor does not exist.
     * @return the XWikiApplication representing application descriptor.
     * @throws XWikiException error when searching for application descriptor document.
     * @see #getApplicationDocument(String, XWikiContext, boolean)
     */
    public XWikiApplication getApplication(String appName, XWikiContext context, boolean validate)
        throws XWikiException
    {
        return (XWikiApplication) newSuperDocument(getApplicationDocument(appName, context,
            validate), context);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Override abstract method using {@link XWikiApplication} as {@link XWikiApplication}.
     * 
     * @see com.xpn.xwiki.plugin.applicationmanager.core.doc.objects.classes.AbstractSuperClass#newSuperDocument(com.xpn.xwiki.doc.XWikiDocument,
     *      com.xpn.xwiki.XWikiContext)
     */
    public SuperDocument newSuperDocument(XWikiDocument doc, XWikiContext context)
    {
        return (SuperDocument) doc.newDocument(XWikiApplication.class.getName(), context);
    }
}
