// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * All possible error causes for error classes.
 * Note that some of these error might never be received under current API level.
 *
 * @hide
 * @since API 1
 */
public enum ErrorCode implements Parcelable {
    // Scan errors
    /**
     * Indicates that one or more provided parameters exceeds the capabilities of the device.
     * The operation was not performed. This status is not used for destination capabilities exceeded issues.
     * These are reported with the Xxx_DESTINATION_CAP_EXCEEDED statuses.
     *
     * @since API 1
     */
    SCAN_JOB_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more provided parameters are in conflict.
     * This status is not used for destination conflicts.
     * These are reported with Xxx_DESTINATION_CONFLICT statuses to assist the client to narrow down the issue.
     *
     * @since API 1
     */
    SCAN_JOB_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates some problem with the scan job destination.
     *
     * @since API 1
     */
    SCAN_JOB_DESTINATION_PROBLEM(Result.SpsCauseKind.CREATION),
    /**
     * The scan job cannot be created because there is a resource problem preventing it from being created.
     *
     * @since API 1
     */
    SCAN_JOB_RESOURCE_NOT_AVAILABLE(Result.SpsCauseKind.CREATION),
    /**
     * Status to indicate the scan job could not be created because there is a conflicting job progressing.
     *
     * @since API 1
     */
    SCAN_JOB_CONFLICTING_JOB(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    SMB_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    SMB_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    EMAIL_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    EMAIL_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    FTP_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    FTP_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    STORAGE_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    STORAGE_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    WEB_DAV_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    WEB_DAV_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    SSIP_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    SSIP_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    STWF_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    STWF_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * This status will occur if automatic size detection is indicated in the job request
     * AND size detection has already completed or is in progress for some reason other than the job request
     * AND such detection failed to determine the size.
     *
     * @since API 1
     */
    ORIGINAL_SIZE_DETECTION_ERROR(Result.SpsCauseKind.CREATION),
    /**
     * POP3 protocol error or any other error during POP3 session.
     *
     * @since API 1
     */
    POP_3_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * Could not connect to configured POP3 server.
     *
     * @since API 1
     */
    POP_3_CONNECTION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * POP3 server login error.
     *
     * @since API 1
     */
    POP_3_AUTHENTICATION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * SMTP protocol error or any other error during SMTP session.
     *
     * @since API 1
     */
    SMTP_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * Could not connect to configured SMTP server.
     *
     * @since API 1
     */
    SMTP_CONNECTION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * SMTP server login error.
     *
     * @since API 1
     */
    SMTP_AUTHENTICATION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * This return status indicates that SMTP has not yet been configured.
     *
     * @since API 1
     */
    SMTP_NOT_CONFIGURED(Result.SpsCauseKind.CREATION),
    /**
     * This return status indicates that the document specified for MFP processing (printing, sending to server, sending to e-mail, etc.)
     * is not in a format compatible with the MFP's ability to provide page image previews.
     *
     * @since API 1
     */
    PREVIEW_NOT_SUPPORTED_FOR_DOCUMENT(Result.SpsCauseKind.CREATION),
    /**
     * The receive or store must terminate early due to a network error.
     *
     * @since API 1
     */
    STORE_NETWORK_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * Email send failed due SMTP error returned. Could be related to SMTP sever authentication being supported but not enabled.
     * Or 500 code is returned from mail server.
     *
     * @since API 1
     */
    EMAIL_SEND_FAILED(Result.SpsCauseKind.PROCESSING),
    /**
     * The email size exceeds the largest allowed.
     *
     * @since API 1
     */
    EMAIL_MAIL_TOO_LARGE(Result.SpsCauseKind.PROCESSING),
    /**
     * The email address does not follow the rules. Such as with space in between, email addresses,
     * starting with special characters like . + - _ @ and Email Id with more than one @ Starting with special characters like . + - _ @.
     *
     * @since API 1
     */
    EMAIL_ADDRESS_MALFORMED(Result.SpsCauseKind.PROCESSING),
    /**
     * The email size exceeds the the server limit or other capabilities.
     *
     * @since API 1
     */
    EMAIL_EXCEEDS_SERVER_CAPABILITIES(Result.SpsCauseKind.PROCESSING),
    /**
     * The POP3 authentication is not configured on the device.
     *
     * @since API 1
     */
    POP_3_AUTHENTICATION_REQUIRED(Result.SpsCauseKind.PROCESSING),
    /**
     * The SMTP authentication is not configured on the device.
     *
     * @since API 1
     */
    SMTP_AUTHENTICATION_REQUIRED(Result.SpsCauseKind.PROCESSING),
    /**
     * This processing info communicates that the indicated sheet and side contained an image detected to be a bank note,
     * and indicates the action that was taken with regard to the image on that page.
     *
     * @since API 1
     */
    SCAN_BANK_NOTE_DETECTED(Result.SpsCauseKind.PROCESSING),
    /**
     * The scanner failed to load the document from the ADF and it was not recoverable.
     *
     * @since API 1
     */
    SCAN_ADF_LOAD_FAIL(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the job had a size detection error.
     * This occurs when automatic sizing is requested but the size of the original cannot be determined.
     *
     * @since API 1
     */
    SCAN_SIZE_DETECTION_FAILURE(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates a scanning document jam took place that was not recoverable.
     *
     * @since API 1
     */
    SCAN_DOCUMENT_JAM(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates a scanning unit was locked.
     *
     * @since API 1
     */
    SCAN_CCD_LOCKED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that intermediate files used for scanning were concurrently modified by some other activity.
     *
     * @since API 1
     */
    SCAN_CONCURRENT_FILE_ACCESS_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that a scan-prohibited page was encountered during scanning.
     *
     * @since API 1
     */
    SCAN_PROHIBITED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the password needed to scan a document was incorrectly entered.
     *
     * @since API 1
     */
    SCAN_INCORRECT_PASSWORD(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that multiple, different password-protected pages were encountered.
     *
     * @since API 1
     */
    SCAN_MIXED_PASSWORD(Result.SpsCauseKind.PROCESSING),
    /**
     * This indicates that, as a part of the associated task's processing, all pages scanned were detected as being blank.
     * Note that this processing info is available only once it is known that there are no more pages to scan,
     * and all scanned pages were detected to be blank.
     *
     * @since API 1
     */
    SCAN_ALL_PAGES_BLANK(Result.SpsCauseKind.PROCESSING),

    /**
     * Indicates that one or more provided parameters exceeds the capabilities of the device.
     * The operation was not performed.
     *
     * @since API 1
     */
    FAX_SEND_JOB_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more provided parameters are in conflict.
     *
     * @since API 1
     */
    FAX_SEND_JOB_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * The fax job cannot be created because there is a resource issue preventing the job from being created.
     *
     * @since API 1
     */
    FAX_JOB_RESOURCE_NOT_AVAILABLE(Result.SpsCauseKind.CREATION),
    /**
     * This return status indicates that the FaxServer has not yet been configured.
     *
     * @since API 1
     */
    FAX_SERVER_NOT_CONFIGURED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more of the destination parameters exceed the capabilities of the device.
     *
     * @since API 1
     */
    FAX_SERVER_DESTINATION_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that two or more destination parameters conflict with each other, or in other words,
     * together they exceed the capabilities of the device.
     *
     * @since API 1
     */
    FAX_SERVER_DESTINATION_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * The fax had a transmission failure or a fax handshaking failure.
     *
     * @since API 1
     */
    FAX_COMMUNICATION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * The remote station was not capable of receiving the transmission or request.
     *
     * @since API 1
     */
    FAX_REMOTE_STATION_INCOMPATIBLE(Result.SpsCauseKind.PROCESSING),
    /**
     * The fax line was busy being used locally.
     *
     * @since API 1
     */
    FAX_LOCAL_OFF_HOOK(Result.SpsCauseKind.PROCESSING),
    /**
     * Fax phone line error.
     *
     * @since API 1
     */
    FAX_LINE_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * The remote station did not pick up the call.
     *
     * @since API 1
     */
    FAX_NO_ANSWER(Result.SpsCauseKind.PROCESSING),
    /**
     * During the course of the fax send or receive, the line was disconnected.
     * This can represent a physical disconnection (cable disconnected),
     * a logical disconnection (dead line), or a simple remote station hang-up.
     *
     * @since API 1
     */
    FAX_LINE_DISCONNECTED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that a fax protocol error occurred during transmission.
     * Fax negotiation was initiated but a failure occurred at some point before the fax completed.
     *
     * @since API 1
     */
    FAX_PROTOCOL_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * The remote station responded with a busy signal.
     *
     * @since API 1
     */
    FAX_REMOTE_OFF_HOOK(Result.SpsCauseKind.PROCESSING),
    /**
     * The remote station reported a memory full.
     *
     * @since API 1
     */
    FAX_REMOTE_STATION_MEMORY_FULL(Result.SpsCauseKind.PROCESSING),
    /**
     * Retry of a previously failed attempt was initiated.
     *
     * @since API 1
     */
    FAX_RETRY_INITIATED(Result.SpsCauseKind.PROCESSING),
    /**
     * When a FaxDestination task is unsuccessful in calling and transmitting to its specified target
     * fax for the maximum number of retries, the system terminates the task.
     *
     * @since API 1
     */
    FAX_RETRIES_EXHAUSTED(Result.SpsCauseKind.PROCESSING),

    /**
     * Indicates that one or more provided parameters exceeds the capabilities of the device. No job was created.
     *
     * @since API 1
     */
    PRINT_JOB_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more provided parameters are in conflict.
     *
     * @since API 1
     */
    PRINT_JOB_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates some problem with the print job source.
     *
     * @since API 1
     */
    PRINT_JOB_SOURCE_PROBLEM(Result.SpsCauseKind.CREATION),
    /**
     * Status type for print job not available due to resource problem.
     *
     * @since API 1
     */
    PRINT_JOB_RESOURCE_NOT_AVAILABLE(Result.SpsCauseKind.CREATION),
    /**
     * This status indicates that the authenticated user making the request is not authorized
     * to access the storage location provided. This can happen, for instance,
     * if the storage location was obtained from a confidential stored job by one authenticated user,
     * but then submitted to this request by a different authenticated user.
     *
     * @since API 1
     */
    PRINT_JOB_UNAUTHORIZED_FILE_STORE(Result.SpsCauseKind.CREATION),
    /**
     * Status indicating that the PDF password supplied was incorrect.
     *
     * @since API 1
     */
    PDF_PASSWORD_INCORRECT(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that there was an error is language processing of the PDL data.
     *
     * @since API 1
     */
    INTERPRET_LANGUAGE_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates there was an error in decoding the PDL data stream.
     *
     * @since API 1
     */
    INTERPRET_DECODING_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that there was insufficient storage (memory or hard disk space) to complete the processing of the PDL data stream.
     *
     * @since API 1
     */
    INTERPRET_STORAGE_FULL(Result.SpsCauseKind.PROCESSING),
    /**
     * This processing info value indicates that permission was denied for the requested access to the PDL document,
     * as defined by permissions for the given document password.
     *
     * @since API 1
     */
    INTERPRET_PERMISSION_DENIED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the document was not stapled due to an out of staples condition.
     *
     * @since API 1
     */
    PRINT_OUT_OF_STAPLES(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the document was not stapled because of exceeding some capability of the stapler such as
     * the limit of sheets or the type of media that can be stapled.
     *
     * @since API 1
     */
    PRINT_STAPLE_CAPABILITY_EXCEEDED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the position of the staple in the finished document is different than requested,
     * due to product limitations of supported staple locations for the loaded media.
     *
     * @since API 1
     */
    PRINT_STAPLE_POSITION_CHANGED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the booklet was not stapled because of exceeding some capability of the booklet
     * maker's staple such as the limit of sheets or the type of media that can be stapled.
     *
     * @since API 1
     */
    PRINT_BOOKLET_STAPLE_CAPABILITY_EXCEEDED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that printing terminated early due to an out of toner condition.
     *
     * @since API 1
     */
    PRINT_TONER_EMPTY(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that the document was not folded because of exceeding some capability of
     * the folder such as limit of sheets or the type of media that can be folded.
     *
     * @since API 1
     */
    PRINT_FOLD_CAPABILITY_EXCEEDED(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates the document was not punched because of exceeding some capability of
     * the hole puncher such as the limit of sheets of the type of media that can be punched.
     *
     * @since API 1
     */
    PRINT_PUNCH_CAPABILITY_EXCEEDED(Result.SpsCauseKind.PROCESSING),

    /**
     * Indicates that one or more provided parameters exceeds the capabilities of the device.
     * The operation was not performed.
     *
     * @since API 1
     */
    COPY_JOB_CAP_EXCEEDED(Result.SpsCauseKind.CREATION),
    /**
     * Indicates that one or more provided parameters are in conflict.
     *
     * @since API 1
     */
    COPY_JOB_CONFLICT(Result.SpsCauseKind.CREATION),
    /**
     * The copy job cannot be created because there is a resource issue preventing the job from being created.
     *
     * @since API 1
     */
    COPY_JOB_RESOURCE_NOT_AVAILABLE(Result.SpsCauseKind.CREATION),
    /**
     * This status indicates that that job's requested copy security parameters are in conflict with the system's copy security override setting
     * and therefore the job could not be created.
     *
     * @since API 1
     */
    COPY_SECURITY_OVERRIDE_CONFLICT(Result.SpsCauseKind.CREATION),

    /**
     * Memory was full so the action couldn't be completed.
     *
     * @since API 1
     */
    COMMON_MEMORY_FULL(Result.SpsCauseKind.PROCESSING),
    /**
     * Indicates that a quota allocation was denied or unavailable.
     *
     * @since API 1
     */
    COMMON_QUOTA_DENIED(Result.SpsCauseKind.PROCESSING),
    /**
     * The credentials could not be authenticated.
     *
     * @since API 1
     */
    COMMON_AUTHENTICATION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * When a held job is terminated by the system due to the hold job timer expiring for the job,
     * the job transitions to a state/substate of jskJobCompleteState/jsskErrored,
     * and each uncompleted task transitions to a state of tskCompletedWithErrors and this processing info value is added to it.
     *
     * @since API 1
     */
    COMMON_HOLD_JOB_TIMEOUT_EXPIRED(Result.SpsCauseKind.PROCESSING),

    /**
     * This indicates that during receiving from or storing to a store location there was
     * the inability to read, create or overwrite a file because of insufficient access rights.
     *
     * @since API 1
     */
    STORE_ACCESS_DENIED(Result.SpsCauseKind.PROCESSING),
    /**
     * The storing failed due to an authentication failure (bad user ID or password) on the storage device or server.
     *
     * @since API 1
     */
    STORE_AUTHENTICATE_FAILURE(Result.SpsCauseKind.PROCESSING),
    /**
     * This indicates that the receive or store terminated early due to a communication
     * error when communicating with the storage device or server.
     *
     * @since API 1
     */
    STORE_COMMUNICATION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * The store must terminate early due to a destination file already existing (and overwriting is not allowed)
     *
     * @since API 1
     */
    STORE_FILE_ALREADY_EXISTS(Result.SpsCauseKind.PROCESSING),
    /**
     * The store must terminate early due to a destination file name being too long.
     *
     * @since API 1
     */
    STORE_FILE_NAME_TOO_LONG(Result.SpsCauseKind.PROCESSING),
    /**
     * The store must terminate early due to an inability to find the specified storage device or server location.
     *
     * @since API 1
     */
    STORE_INVALID_LOCATION(Result.SpsCauseKind.PROCESSING),
    /**
     * The receive or store must terminate early due to an I/O error. (e.g. file I/O error, network I/O error)
     *
     * @since API 1
     */
    STORE_IO_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * The receive or store must terminate early because the storage medium was removed
     * (applicable only to removable media, such as USB memory devices).
     *
     * @since API 1
     */
    STORE_MEDIUM_REMOVED(Result.SpsCauseKind.PROCESSING),
    /**
     * The store must terminate early due to the storage medium being full
     *
     * @since API 1
     */
    STORE_MEDIUM_FULL(Result.SpsCauseKind.PROCESSING),
    /**
     * The store must terminate early due to concurrent (destructive) access to files being stored.
     *
     * @since API 1
     */
    STORE_CONCURRENT_FILE_ACCESS_ERRORL(Result.SpsCauseKind.PROCESSING),
    /**
     * There is already a lock directory. (.lck)
     *
     * @since API 1
     */
    STORE_LOCK_EXISTS(Result.SpsCauseKind.PROCESSING),

    /**
     * Can not contact LDAP server.
     *
     * @since API 1
     */
    LDAP_COMMUNICATION_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * LDAP server returned an error during transmission.
     * Could be related to LDAP server authentication being supported but not enabled on the device. Or maximum search results exceed limits.
     *
     * @since API 1
     */
    LDAP_SEARCH_ERROR(Result.SpsCauseKind.PROCESSING),
    /**
     * The LDAP search time has exceeded the timeout parameter.
     *
     * @since API 1
     */
    LDAP_SEARCH_TIME_OUT(Result.SpsCauseKind.PROCESSING),
    /**
     * LDAP sever can not match the user entry.
     *
     * @since API 1
     */
    LDAP_NO_USER_MATCH(Result.SpsCauseKind.PROCESSING),
    /**
     * Authentication has not performed to the correct level in the MFP so the job can't be completed.
     *
     * @since API 1
     */
    AUTHENTICATION_REQUIRED(Result.SpsCauseKind.PROCESSING),
    /**
     * DNS resolution error or DNS server not reachable.
     *
     * @since API 1
     */
    DNS_ERROR(Result.SpsCauseKind.PROCESSING),

    // Additional custom errors
    /**
     * Failed to finish job based on PDL protocol.
     *
     * @since API 1
     */
    FINISHING_PDL_JOB_FAILED(Result.SpsCauseKind.PROCESSING),
    /**
     * Response for Job creation hasn't been received
     *
     * @since API 1
     */
    UNABLE_TO_GET_SUBMISSION_RESULT(Result.SpsCauseKind.CREATION),
    /**
     * Response for Job creation was incomplete
     *
     * @since API 1
     */
    INCOMPLETE_JOB_CREATION_RESPONSE(Result.SpsCauseKind.CREATION),
    /**
     * Response for Job receipt receiving failed
     *
     * @since API 1
     */
    FAILED_TO_GET_JOB_RECEIPT(Result.SpsCauseKind.PROCESSING),

    // Cancel, resume, complete, hold, pause, promote, release statuses
    /**
     * The structure is used for status to indicate the request couldn't
     * be performed because the job was not a state for which the operation is allowed.
     *
     * @since API 1
     */
    INVALID_JOB_STATE(Result.SpsCauseKind.OPERATION),
    /**
     * This is a a status to indicate the implementation does not support the requested operation.
     *
     * @since API 1
     */
    OPERATION_NOT_SUPPORTED(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication failed due to an inability to validate the provided information
     * (for example, no such user, or user exists but password is incorrect).
     * The specific reason for the failure is not communicated for security reasons.
     *
     * @since API 1
     */
    AUTH_FAILURE(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication with an external server failed for a general reason.
     * Information from the external server is made available,
     * but can only be interpreted if the external authentication protocol is understood by the client.
     *
     * @since API 1
     */
    REMOTE_AUTH_FAILURE(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication with an external server failed for a general reason.
     * Information from the external server is made available,
     * but can only be interpreted if the external authentication protocol is understood by the client.
     *
     * @since API 1
     */
    LOCKED_OUT(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that verification of the password succeeded, but that the user's password has expired. In this case, authentication has failed.
     *
     * @since API 1
     */
    PASSWORD_EXPIRED(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication failed because the password did not meet minimum requirements, for example,
     * if the user name and password are identical.
     * A detailed reason for this error is not provided, as this would weaken overall system security.
     *
     * @since API 1
     */
    PASSWORD_ERROR(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication failed because the selected provider requires a card reader,
     * and the card reader is not attached.
     *
     * @since API 1
     */
    NO_CARD_READER(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication failed because the selected provider requires a card to be inserted
     * and no card was inserted within a configured timeout period.
     *
     * @since API 1
     */
    NO_CARD(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that authentication failed because the selected provider requires
     * a card to be inserted and while a card was inserted, it cannot be read.
     *
     * @since API 1
     */
    CARD_READ_ERROR(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that a card was swiped/tapped/inserted,
     * but that the card is not registered in the database (and that self-registration is not allowed).
     *
     * @since API 1
     */
    CARD_NOT_REGISTERED(Result.SpsCauseKind.OPERATION),
    /**
     * Indicates that a time out occurred during authentication.
     * This simply indicates that authentication was not completed in a timely manner.
     *
     * @since API 1
     */
    AUTH_TIMEOUT(Result.SpsCauseKind.OPERATION),
    /**
     * This status indicates that the authentication sequence was terminated (unsuccessfully)
     * because the authentication options associated with
     * the provider have been changed in some way that invalidates the current authentication sequence.
     * Clients should create a new authentication sequence to restart authentication.
     *
     * @since API 1
     */
    AUTH_RECONFIGURED(Result.SpsCauseKind.OPERATION);

    /**
     * Kind of this error, when the error has occurred
     */
    public final Result.SpsCauseKind kind;

    /**
     * Default constructor
     */
    ErrorCode(final Result.SpsCauseKind kind) {
        this.kind = kind;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(name());
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Creator<ErrorCode> CREATOR = new Creator<ErrorCode>() {
        @Override
        public ErrorCode createFromParcel(final Parcel source) {
            return ErrorCode.valueOf(source.readString());
        }

        @Override
        public ErrorCode[] newArray(final int size) {
            return new ErrorCode[size];
        }
    };
}
