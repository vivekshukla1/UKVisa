package com.ilinksolutions.UKVisaDb.data;

import java.util.List;

import com.ilinksolutions.UKVisaDb.domains.UKVisaMessage;

/**
 *
 */
public interface UKVisaDAO
{
	UKVisaMessage save(UKVisaMessage entry);
    List<UKVisaMessage> list();
    UKVisaMessage getEntry(int id);
    UKVisaMessage updateEntry(int id, UKVisaMessage message);
}
