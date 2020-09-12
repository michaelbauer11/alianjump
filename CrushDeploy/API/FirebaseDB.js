import firestore from '@react-native-firebase/firestore';

/* NOTES:

1. When a function asks for a collection / a document it means only 
   the NAME Of that collection / document, not an object of that doc/collection

2. Our plan in firebase is limiting our reads/writes to the DB, so when
   writing code we should minimize the amount of usage in these functions.

*/

// Returns a collection in our DB.
export const getCollection = async (collection) => {
    const collectionSnapshot = await firestore().collection(collection).get();
    return collectionSnapshot;
}

// Returns a specific document in a collection
export const getDocument = async (collection, doc) => {
    const documentSnapshot = await firestore().collection(collection).doc(doc).get();
    return documentSnapshot;
}

// Returns an array with the names of all documents in a specific collection
export const getAllDocumentsNames = async (collection) => {
    let collectionSnapshot = await getCollection(collection);
    let allDocuments = collectionSnapshot.docs;
    let docsNames = [];

    for (var i = 0; i < allDocuments.length; i++) {
        docsNames.push(allDocuments[i].id);
    }

    return docsNames;
}

// Returns an array with only the data of all documents in a specific collection
export const getAllDocumentsData = async (collection) => {
    let collectionSnapshot = await getCollection(collection);
    let allDocuments = collectionSnapshot.docs;
    let docsData = [];

    for (var i = 0; i < allDocuments.length; i++) {
        docsData.push(allDocuments[i].data());
    }

    return docsData;
}

// Returns an array with all documents with their data in the form of [name, data]
export const getDocumentsDataPairs = async (collection) => {
    let collectionSnapshot = await getCollection(collection);
    let allDocuments = collectionSnapshot.docs;
    let docsPairs = [];

    for (var i = 0; i < allDocuments.length; i++) {
        docsPairs.push({"name": allDocuments[i].id, "data": allDocuments[i].data()});
    }

    return docsPairs;
}

// Returns the data of a given document inside a given collection
export const getDocumentData = async (collection, doc) => {
    let document = await getDocument(collection, doc);
    return document.data();
}

// Sets a document's data to a given value.
// GOOD NEWS : If the document doesnt exist, it will create it with the name given in the parameters,
// so that function is also used for creating new documents
export const setDocumentData = async (collection, document, documentData) => {
    let doc = await firestore().collection(collection).doc(document);
    doc.set(documentData);
    return true;
}
