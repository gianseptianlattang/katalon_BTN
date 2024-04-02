import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword as WebUIAbstractKeyword
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('')

WebUI.maximizeWindow()

WebUI.navigateToUrl('https://www.btnproperti.co.id/tools/hitung-harga-properti')

int rowPositif

int randomValue

for (rowPositif = 1; rowPositif <= 5; rowPositif++) {
    long penghasilan = Long.parseLong(findTestData('Data Test BTN').getValue('Penghasilan', rowPositif))

    long pengeluaran = Long.parseLong(findTestData('Data Test BTN').getValue('Pengeluaran', rowPositif))

    WebUI.setText(findTestObject('Page Hitung Harga Properti Maksimal BTN/Input Penghasilan'), penghasilan.toString())

    WebUI.setText(findTestObject('Page Hitung Harga Properti Maksimal BTN/Input Pengeluaran'), pengeluaran.toString())

    randomValue = (new Random().nextInt(30) + 1)

    WebUI.selectOptionByValue(findTestObject('Page Hitung Harga Properti Maksimal BTN/Dropdown Jangka Waktu'), randomValue.toString(), 
        false)

    WebUI.click(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))

    String elementText = WebUI.getText(findTestObject('Page Hitung Harga Properti Maksimal BTN/Text Harga Properti Maksimal'))

    long actualResult = Long.parseLong(elementText.replaceAll('[^\\d]', ''))

    println("Text value: $penghasilan $pengeluaran $randomValue")

    long expectedResult = ((penghasilan - pengeluaran) * (randomValue * 12)) / 3

    println("Text value of the actualResult: $actualResult")

    println("Text value of the expectedResult: $expectedResult")

    def testCaseValue = findTestData('Data Test BTN').getValue('Test Case', rowPositif)

    def assertionMessage

    try {
        assert actualResult == expectedResult

        WebUI.takeScreenshot([('text') : testCaseValue + ' - Passed', ('x') : 50, ('y') : 750, ('fontSize') : 28, ('fontColor') : '#65B741' // Green color
            ])

        assertionMessage = "Assertion Passed: Actual result ($actualResult) matches expected result ($expectedResult)"
    }
    catch (AssertionError e) {
        WebUI.takeScreenshot([('text') : testCaseValue + ' - Failed', ('x') : 50, ('y') : 750, ('fontSize') : 28, ('fontColor') : '#FF0000' // Red color
            ])

        assertionMessage = "Assertion Failed: Actual result ($actualResult) does not match expected result ($expectedResult)"
    } 
    
    println(assertionMessage)
}

for (int rowNegatif = rowPositif; rowNegatif <= findTestData('Data Test BTN').getRowNumbers(); rowNegatif++) {
    WebUI.refresh()

    WebUI.setText(findTestObject('Page Hitung Harga Properti Maksimal BTN/Input Penghasilan'), findTestData('Data Test BTN').getValue(
            'Penghasilan', rowNegatif))

    WebUI.setText(findTestObject('Page Hitung Harga Properti Maksimal BTN/Input Pengeluaran'), findTestData('Data Test BTN').getValue(
            'Pengeluaran', rowNegatif))

    randomValue = (new Random().nextInt(30) + 1)

    println("Text value of the rowNegatif: $rowNegatif")

    if (rowNegatif <= (findTestData('Data Test BTN').getRowNumbers() - 2)) {
        WebUI.selectOptionByValue(findTestObject('Page Hitung Harga Properti Maksimal BTN/Dropdown Jangka Waktu'), randomValue.toString(), 
            false)
    } else if (rowNegatif == (findTestData('Data Test BTN').getRowNumbers() - 1)) {
        WebUI.selectOptionByValue(findTestObject('Page Hitung Harga Properti Maksimal BTN/Dropdown Jangka Waktu'), randomValue.toString(), 
            false)

        String actualTextValidasi = WebUI.getText(findTestObject('Page Hitung Harga Properti Maksimal BTN/Warning Validasi Pengeluaran Lebih Besar'))

        try {
            assert actualTextValidasi == 'Isi kurang dari nilai sebelumnya'

            println("Assertion Passed: Actual text '$actualTextValidasi' matches expected text 'Isi kurang dari nilai sebelumnya'")
        }
        catch (AssertionError e) {
            println("Assertion Failed: Actual text '$actualTextValidasi' does not match expected text 'Isi kurang dari nilai sebelumnya'")
        } 
    }
    
    try {
        WebUI.verifyElementNotClickable(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))

        println('Assertion Passed: Element is not clickable')
    }
    catch (Exception e) {
        println('Assertion Failed: Element is clickable')
    } 
    
    def testCaseValue = findTestData('Data Test BTN').getValue('Test Case', rowNegatif)

    def assertionMessage

    try {
        WebUI.verifyElementNotClickable(findTestObject('Page Hitung Harga Properti Maksimal BTN/Button Hitung'))

        WebUI.takeScreenshot([('text') : testCaseValue + ' - Passed', ('x') : 50, ('y') : 750, ('fontSize') : 32, ('fontColor') : '#65B741' // Green color
            ])

        println('Assertion Passed: Element is not clickable')
    }
    catch (Exception e) {
        WebUI.takeScreenshot([('text') : testCaseValue + ' - Failed', ('x') : 50, ('y') : 750, ('fontSize') : 32, ('fontColor') : '#FF0000' // Red color
            ])

        println('Assertion Failed: Element is clickable')
    } 
}

WebUI.closeBrowser(FailureHandling.CONTINUE_ON_FAILURE)